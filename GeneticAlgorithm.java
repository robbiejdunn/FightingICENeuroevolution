import java.io.*;
import java.nio.file.Files;
import java.util.Random;

/**
 * Created by Robbie on 07/03/2017.
 */
public class GeneticAlgorithm {

    private static String EXPERIMENT_NAME = "P2EVO02";
    private static int NUM_WEIGHTS = 267;
    private static int POPULATION_SIZE = 5;
    private static double MUTATION_RATE = 0.05;
    private static double CROSSOVER_RATE = 0.5;
    private static int NUM_GENERATIONS = 100;

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
        if (results[0] + results[1] + results[2] != 0) {        /* Prevents dividing with 0 errors */
            fitness = ((results[0] + results[1] + results[2]) / 3) + 800;
        }
        else {
            fitness = 500;
        }
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

    /* Uses fitness proportionate selection (roulette wheel selection) to crossover parents */
    public double[] proportionSelect(double[][] popcur, double[] props) {
        double r = rand.nextDouble();
        int p1 = 0, p2 = 0;             // Indexes of the parents chosen for crossover
        double[] child = new double[NUM_WEIGHTS];

        /* Select first parent for crossover */
        for (int i = 0; i < POPULATION_SIZE; i++) {
            r -= props[i];
            if (r <= 0) {
                p1 = i;
            }
        }

        /* Second parent for crossover */
        r = rand.nextDouble();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            r -= props[i];
            if (r <= 0) {
                p2 = i;
            }
        }

        /* Create child */
        for (int i = 0; i < 150; i++) {
            child[i] = popcur[p1][i];
        }
        for (int i = 150; i < NUM_WEIGHTS; i++) {
            child[i] = popcur[p2][i];
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

            /* Read population weights and store in readable (array) form */
            for (int i = 0; i < POPULATION_SIZE; i++) {
                String[] tmp = br.readLine().split(",");
                for (int j = 0; j < NUM_WEIGHTS; j++) {
                    populationCur[i][j] = Double.parseDouble(tmp[j]);
                    System.out.println(populationCur[i][j]);
                }
            }

            /* Calculate proportions for selection */
            int total = 0;
            int tmp;
            for (int i = 0; i < POPULATION_SIZE; i++) {
                tmp = Integer.parseInt(br.readLine());
                proportions[i] = tmp;
                total += tmp;
            }
            System.out.println("Total fitness for proportionate selection: " + total);
            br.close();
            for (int i = 0 ; i < POPULATION_SIZE; i++) {
                proportions[i] /= total;
                System.out.println("Proportion " + i + ": " + proportions[i]);
            }

            /* Generate next population */
            PrintWriter pw = new PrintWriter(EXPERIMENT_NAME + "\\generation" + generation + ".txt");
            System.out.println("Created file for generation " + generation);
            for (int i = 0; i < POPULATION_SIZE; i++) {
                if (rand.nextDouble() > CROSSOVER_RATE) {
                    System.out.println("Individual " + i + " created with crossover.");
                    populationNew[i] = proportionSelect(populationCur, proportions);
//                    System.out.println(populationNew[i].toString());
                }
                else {
                    System.out.println("Individual " + i + " unchanged.");
                    for ( int j = 0; j < NUM_WEIGHTS; j++) {
                        populationNew[i][j] = populationCur[i][j];
                    }
//                    System.out.println(populationNew[i].toString());
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
