import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import structs.Key;

/**
 * NeuralNet
 *
 * An artificial neural network using the deeplearning4j java library. Used by class Controller. Network consists of
 * 6 inputs nodes, 20 hidden layer and 7 output. Provides methods to initialise the network and feed through the
 * network with inputs of game frame data.
 *
 * Created by Robbie on 05/03/2017.
 */
public class NeuralNetwork {

    MultiLayerNetwork net;

    public NeuralNetwork() {
        init();
    }

    public void init() {
        int seed = 123;         // seed for weight init
        int numInputs = 5;      // 6 inputs from frame data
        int numHidden = 20;     // number of nodes in the hidden layer of the network
        int numOutputs = 7;     // 7 possible outputs for agent

        /* configuration for the neural net */
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(1)
                .list()
                .layer(0, new DenseLayer.Builder()          // layer 0: input->hidden
                        .nIn(numInputs)
                        .nOut(numHidden)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.TANH)
                        .build())
                .layer(1, new OutputLayer.Builder()          // layer 1: hidden->output
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.TANH)
                        .weightInit(WeightInit.XAVIER)
                        .nIn(numHidden)
                        .nOut(numOutputs)
                        .build())
                .backprop(false).pretrain(false).build();

        /* initialising neural net */
        net = new MultiLayerNetwork(conf);
        net.init();
        System.out.println("Network initialised: "+numInputs+" input nodes, "+numHidden+" hidden and "
            +numOutputs+" output.\n");
    }




    public double[] feed(int myX, int myY, int enemyX, int enemyY, int energy) {
        double[] output;
        /* normalises and scales inputs accordingly */
        double myXNorm = (((double) (myX + 120) / (double) 800) * 2) - 1;           // x range -120/680
        double myYNorm = (((double) (-myY + 335) /  (double) 465) * 2) - 1;         // y range 335/-130
        double enemyXNorm = (((double) (enemyX + 120) / (double) 800) * 2) - 1;     // x as above
        double enemyYNorm = (((double) (-enemyY + 335) / (double) 465) * 2) - 1;    // y as above
        double energyNorm = (((double) energy / (double) 1000) * 2) - 1;            // energy range 0/1000
        
        System.out.println("Network feed:\tmy x="+myXNorm+",my y="+myYNorm+",enemy x="+enemyXNorm
            +",enemy y="+enemyYNorm+",my energy="+energyNorm);

        double[] vectorDoubles = new double[]{myXNorm, myYNorm, enemyXNorm, enemyYNorm, energyNorm};
        INDArray ia = Nd4j.create(vectorDoubles);
//        System.out.println(net.params());
        output = new double[7];
        INDArray fed = net.feedForward(ia).get(2);
        for (int i = 0; i < output.length; i++) {
            output[i] = fed.getDouble(i);
        }
        return output;
    }

    public static void main(String[] args) {
        NeuralNetwork n = new NeuralNetwork();
        n.feed(0,0,0,0,0);
    }

}
