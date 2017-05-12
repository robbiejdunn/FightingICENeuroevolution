import java.util.Random;
import structs.Key;
/**
 * Implements a 3-layer artificial neural network with 3 input
 * nodes, 10 hidden layer nodes and 7 output nodes. Output nodes
 * refer to the 7 possible key presses; input nodes are defaultly
 * horizontal distance, vertical distance and player energy.
 * 
 * @author Robbie Dunn
 *
 */
public class ANN {
  
  private double[][] weights0;    // weights for 0th network layer
  private double[][] weights1;    // weights for 1st network layer

  /* declares and initialises weights randomly (-1.0, 1.0) */
  public ANN() {
    weights0 = new double[3][10];
    weights1 = new double[10][7];
    Random rand = new Random();
    
    // initialising weights
    for(int i=0;i<10;i++) {   // for each hidden layer node
      for(int j=0;j<3;j++) {  // for each input layer node
        weights0[j][i] = (rand.nextDouble() * 2) - 1; // weight initiailised between -1/1
      }
      for(int k=0;k<7;k++) {  // for each output layer node
        weights1[i][k] = (rand.nextDouble() * 2) - 1;
      }
    }
  }
  
  /* feed through the network with x-distance, y-distance and energy */
  public double[] feed(double x, double y, double e) {
    double[] hid = new double[10];  // hidden layer nodes
    double[] out = new double[7]; // output layer nodes
    for(int i=0;i<10;i++) {   // feed through to hidden layer
      hid[i] = (double) (weights0[0][i] * x       // X
            + weights0[1][i] * y          // Y
            + weights0[2][i] * e);          // energy
    }
    for(int j=0;j<7;j++) {
      out[j] = 0;
      for(int k=0;k<10;k++) {
        out[j] += (weights1[k][j] * hid[k]);
      }
      System.out.println(j+"th output: "+out[j]);
    }
    return out;
  }
  
}
