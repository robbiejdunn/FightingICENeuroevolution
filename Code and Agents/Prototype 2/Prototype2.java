import commandcenter.CommandCenter;
import gameInterface.AIInterface;
import structs.FrameData;
import structs.GameData;
import structs.Key;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Robbie on 01/03/2017.
 */
public class Prototype2 implements AIInterface {
    boolean p;                          // Player number of agent
    CommandCenter cc;                   // Provides methods to read game state & call commands
    FrameData fd;                       // Provides methods to read frame data
    GameData gd;                        // Provides game information e.g. combo tables
    Key inputKey;                       // Output key(s) for the agent

    NeuralNetwork nn;                   // For feeding through network and loading weights (genotype)
    GeneticAlgorithm genAlg;            // Saves evolution data to csv files & loads neural net genotypes
    int[] roundResults;
    boolean demo;

    /* Creates the neural net and genetic algorithm. Initialises game data */
    @Override
    public int initialize(GameData gameData, boolean playerNumber) {
        gd = gameData;
        p = playerNumber;
        cc = new CommandCenter();
        fd = new FrameData();
        inputKey = new Key();

        nn = new NeuralNetwork();
        demo = false;
        boolean run = true;

        File demof = new File(".\\P2EVO02\\demo.txt");
        if (demof.exists() && !demof.isDirectory()) {
            try {
                System.out.println("Initialising demo");
                demo = true;
                BufferedReader br = new BufferedReader(new FileReader(demof));
                String[] demoSWeights = br.readLine().split(",");
                double[] demoWeights = new double[demoSWeights.length];
                for (int i = 0; i < demoSWeights.length; i++) {
                    demoWeights[i] = Double.parseDouble(demoSWeights[i]);
                }
                nn.updateWeights(demoWeights);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            genAlg = new GeneticAlgorithm();
            roundResults = new int[3];
            double[] genotype = genAlg.next();
            if (genotype != null) {
                nn.updateWeights(genotype);
            } else {
                close();
            }
        }
        System.out.println("Neuroevolution agent initialised.");
        return 0;
    }

    @Override
    public void getInformation(FrameData frameData) {
        fd = frameData;
        cc.setFrameData(fd, p);
    }



    @Override
    public void processing() {

        if(!fd.getEmptyFlag()){         // there is frame data available
            if(fd.getRemainingTime() > 0){      // frame data has not expired
                if (fd.getFrameNumber() % 10 == 0) {
                    //System.out.println("Frame " + fd.getFrameNumber() + " processed.");
                    if (fd.getRemainingFrame() <= 20 && demo == false) {
                        System.out.println("Round " + fd.getRound() + " finished.");
                        System.out.println("\tAgent HP: " + cc.getMyHP());
                        System.out.println("\tOpponent HP: " + cc.getEnemyHP());
                        roundResults[fd.getRound()] = cc.getMyHP() - cc.getEnemyHP();
                        if (fd.getRound() == 2) {
                            genAlg.eval(roundResults);
                        }
                    }
                    inputKey.empty();
                    inputKey.A = false;
                    inputKey.B = false;
                    inputKey.C = false;
                    inputKey.U = false;
                    inputKey.D = false;
                    inputKey.L = false;
                    inputKey.R = false;
                }
                else if (fd.getFrameNumber() % 5 == 0) {
                    //System.out.println("5!! Frame " + fd.getFrameNumber());
                    inputKey.empty();
                    double[] out = nn.feed((cc.getMyX() - cc.getEnemyX()), (cc.getMyY() - cc.getEnemyY()), cc.getMyEnergy());
                    // feeds through network
//                    System.out.println(out[0] + " " + out[1] + " " + out[2] + " " + out[3] + " " + out[4]);

                    inputKey.L = (out[2] < 0) ? true : false;
                    inputKey.R = (out[3] < 0) ? true : false;
                    inputKey.U = (out[0] < 0) ? true : false;
                    inputKey.D = (out[2] < 0) ? true : false;
                    inputKey.A = (out[4] < 0) ? true : false;
                    inputKey.B = (out[5] < 0) ? true : false;
                    inputKey.C = (out[6] < 0) ? true : false;
//                    if (out[4] < 0) {
//                        inputKey.A = true;
//                        inputKey.B = false;
//                        inputKey.C = false;
//                    } else if (out[5] < 0) {
//                        inputKey.B = true;
//                        inputKey.A = false;
//                        inputKey.C = false;
//                    } else if (out[6] < 0) {
//                        inputKey.C = true;
//                        inputKey.A = false;
//                        inputKey.B = false;
//                    }
//                    System.out.println("UP "+inputKey.U+" D "+inputKey.D+" L "+inputKey.L+" R "+inputKey.R+" A "
//                            +inputKey.A+" B "+inputKey.B+" C "+inputKey.C);
                }
            }
        }
    }

    @Override
    public Key input() {
        // TODO Auto-generated method stub
        return inputKey;
    }

    @Override
    public void close() {
        System.out.println("Game closed.");
    }


    public String getCharacter(){
        return CHARACTER_ZEN;
    }
}