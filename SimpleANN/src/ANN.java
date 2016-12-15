import java.util.Random;

public class ANN {
	
	double[][] weights;
	Random rand;
	
	public ANN(int layerc) {
		weights = new double[2][5];
		rand = new Random();
		
		for (int i=0; i<2; i++) {
			for (int j=0; j<5; j++) {
				weights[i][j] = rand.nextDouble();
			}
		}
	}
	
	public double[] feed(double[] inputs) {
		double[] outs = new double[5];
		for (int i=0; i<5; i++) {
			for (int j=0; j<2; j++) {
				outs[i] = inputs[j]*weights[j][i];
			}
		}
		return outs;
	}
	
}
