import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Robbie on 07/03/2017.
 */
public class GeneticAlgorithm {

    private static String EXPERIMENT_NAME = "P2EVO02";
    private static int NUM_WEIGHTS = 227;
    private static int POPULATION_SIZE = 10;
    private static double MUTATION_RATE = 0.4;
    private static double CROSSOVER_RATE = 0.7;
    private static int NUM_GENERATIONS = 300;
    private static int ELITISM = 3;

    static Random rand;
    int best;
    int generation;

    public GeneticAlgorithm() {
        best = -9999;
        rand = new Random();
    }

    /* Create a tracker file to determine which genotype is next to evaluate */
    private int createTracker() {
        try {
            PrintWriter writer0 = new PrintWriter(".\\"+EXPERIMENT_NAME+"\\genotracker.txt");
            for (int i = 0; i < NUM_GENERATIONS; i++) {
                for (int j = 0; j < POPULATION_SIZE; j++) {
                    writer0.println(i + "," + j);
                }
            }
            writer0.close();
            System.out.println("Genotype tracker file created.");
            return 0;
        }
        catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public double[] next() {
        File exp = new File(EXPERIMENT_NAME);
        double[] next = new double[NUM_WEIGHTS];
        if (!exp.exists()) {
            if (generatePop(exp) != 0) {            // If errors when generating initial population
                return null;
            }
        }
        else {
            System.out.println("Continuing with experiment " + EXPERIMENT_NAME + ".");
        }
        try {
            /* Read from genotype tracker file to find next genotype for evolution */
            BufferedReader br = new BufferedReader(new FileReader(EXPERIMENT_NAME+"/genotracker.txt"));
            String[] geno = br.readLine().split(",");
            br.close();
            System.out.println("Loading genotype:");
            System.out.println("\tGeneration " + geno[0]);
            System.out.println("\tIndividual " + geno[1]);
            PrintWriter pw = new PrintWriter(EXPERIMENT_NAME + "/genotracker.txt");
            int genCur = Integer.parseInt(geno[0]);
            generation = Integer.parseInt(geno[0]);
            int indCur = Integer.parseInt(geno[1]);
            if (indCur == (POPULATION_SIZE - 1)) {             // Last individual in population
                if (genCur == (NUM_GENERATIONS - 1)) {
                    // Evolution complete
                    System.out.println("Agent evolution complete.");
                    return null;
                }
                // EVOLVE POPULATION
                System.out.println("Final genotype of generation " + generation);
                pw.println((genCur + 1) + ",0");
            }
            else {
                pw.println(genCur + "," + (indCur + 1));
            }
            if (indCur == 0 && generation != 0) {
                evolve();
            }
            pw.close();
            br = new BufferedReader(new FileReader(EXPERIMENT_NAME+"/generation"+geno[0]+".txt"));
            for (int i = 0; i < Integer.parseInt(geno[1]); i++) {
                br.readLine();
            }
            geno = br.readLine().split(",");
            for (int i = 0; i < NUM_WEIGHTS; i++) {
                next[i] = Double.parseDouble(geno[i]);
            }
            br.close();
            return next;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Generates the initial population */
    public int generatePop(File exp) {
        try {
            if (!exp.mkdir()) {
                System.out.println("Error: Unable to create experiment directory.");
                return -1;
            }
            System.out.println("Directory for experiment " + EXPERIMENT_NAME + " created.");

            if (createTracker() != 0) {
                System.out.println("Error: Unable to create tracker file.");
                return -1;
            }
            System.out.println("File for genotype tracking created.");
            PrintWriter writer = new PrintWriter(EXPERIMENT_NAME+"\\generation0.txt");
            double[] weights = new double[NUM_WEIGHTS];
            for (int i = 0; i < POPULATION_SIZE; i++) {
                for (int j = 0; j < NUM_WEIGHTS; j++) {
                    weights[j] = (rand.nextDouble() * 2) - 1;
                    writer.print(weights[j]);
                    if (j != (NUM_WEIGHTS - 1)) {
                        writer.print(",");
                    }
                }
                writer.println();
            }
            writer.close();
            System.out.println("Generation 0 generated successfully.");
            return 0;
        }
        catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

    }


    public void eval(int[] results) {
        int fitness;
            fitness = results[0] + results[1] + results[2];

        System.out.println("Fitness:" + fitness);

        try {
            /* Append to fitness file */
            FileWriter fw = new FileWriter(new File(EXPERIMENT_NAME + "/generation" + generation + ".txt"), true);
            fw.write(Integer.toString(fitness) + "\n");
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Selects and returns singular parent for crossover using Tournament Selection */
    public Individual tournamentSelection(Individual[] pops) {
        Individual best = null;
        for (int i = 0; i < 2; i++) {
            /* Choose next random individual for tournament */
            Individual next = pops[rand.nextInt(POPULATION_SIZE)];
            if (best == null || next.getFitness() > best.getFitness()) {
                best = next;
            }
        }
        System.out.println("Tournament selection: parent of fitness " + best.getFitness() +
            " chosen.");
        return best;
    }

    public Individual crossover(Individual p1, Individual p2) {
        Individual child = new Individual();            // Child
        double alpha = rand.nextDouble();
        for (int i = 0; i < NUM_WEIGHTS; i++) {
            child.setWeight(i,
                    (alpha * p1.getWeight(i) + (1 - alpha) * p2.getWeight(i)));
        }
        return child;
    }

    /* Evolves the current population */
    public void evolve() {
        try {
            System.out.println("Evolving generation " + generation);
            BufferedReader br =
                    new BufferedReader(new FileReader(EXPERIMENT_NAME + "\\generation" + (generation - 1) + ".txt"));
            double[] proportions = new double[POPULATION_SIZE];
            double[][] populationCur = new double[POPULATION_SIZE][NUM_WEIGHTS];
            double[][] populationNew = new double[POPULATION_SIZE][NUM_WEIGHTS];

            Individual[] currentPop = new Individual[POPULATION_SIZE];
            /* Read population weights and store in readable (array) form */
            for (int i = 0; i < POPULATION_SIZE; i++) {
                String[] tmp = br.readLine().split(",");
                for (int j = 0; j < NUM_WEIGHTS; j++) {
                    populationCur[i][j] = Double.parseDouble(tmp[j]);
                }
                currentPop[i] = new Individual(populationCur[i]);
            }

            /* Calculate proportions for selection */
            int total = 0;
            int tmp;
            for (int i = 0; i < POPULATION_SIZE; i++) {
                tmp = Integer.parseInt(br.readLine());
                currentPop[i].setFitness(tmp);
                System.out.println("Individual "+i+" with fitness "+currentPop[i].getFitness());
            }
            br.close();

            List<Individual> cpop = Arrays.asList(currentPop);
            Collections.sort(cpop);
            System.out.println("Applying elitism... best individual fitness: " + cpop.get(0).getFitness());

            /* Generate next population */
            PrintWriter pw = new PrintWriter(EXPERIMENT_NAME + "\\generation" + generation + ".txt");
            System.out.println("Created file for generation " + generation);
            for (int i = 0; i < ELITISM; i++) {
                populationNew[i] = cpop.get(i).getWeight();
                pw.print(populationNew[i][0]);
                for (int j = 1; j < NUM_WEIGHTS; j++) {
                    pw.print("," + populationNew[i][j]);
                }
                pw.println();
            }
            for (int i = ELITISM; i < POPULATION_SIZE; i++) {
                if (rand.nextDouble() < CROSSOVER_RATE) {
                    System.out.println("Individual " + i + " created with crossover.");
                    //populationNew[i] = proportionSelect(populationCur, proportions);
                    populationNew[i] =
                            crossover(tournamentSelection(currentPop), tournamentSelection(currentPop)).getWeight();
                    //populationNew[i] = tournamentSelection(currentPop).getWeight();
//                    System.out.println(populationNew[i].toString());
                }
                else {
                    System.out.println("Individual " + i + " unchanged.");
                    for ( int j = 0; j < NUM_WEIGHTS; j++) {
                        populationNew[i][j] = populationCur[i][j];
                    }
//
//          System.out.println(populationNew[i].toString());
                }

                /* Apply mutation */
                mutate(populationNew[i]);
                pw.print(populationNew[i][0]);
                for (int j = 1; j < NUM_WEIGHTS; j++) {
//                    System.out.println(populationNew[i][j]);
                    pw.print("," + populationNew[i][j]);
                }
                pw.println();
            }
            pw.close();

        }
        catch (IOException e) {

        }
    }

    /* mutates the argument value with either an increment or decrement of 0.05 */
    public double[] mutate(double[] geno) {
        for (int i = 0; i < NUM_WEIGHTS; i++) {
            if (rand.nextDouble() > MUTATION_RATE) {
//                System.out.println("Gene mutation.");
                geno[i] = (rand.nextDouble() * 2) - 1;
            }
        }
        return geno;
    }

    public static void main(String[] args) {
        GeneticAlgorithm test = new GeneticAlgorithm();
        test.next();
    }
}
