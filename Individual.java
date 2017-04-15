/**
 * Class Individual
 *
 * Represents an individual of the population used in the evolutionary algorithm.
 *
 * Created by Robbie on 07/03/2017.
 */
public class Individual {

    double[] weights;
    int fitness;

    public Individual(double[] weights) {
        this.weights = weights;
    }

    public int getFitness() {
        return fitness;
    }

    public double[] getWeight() {
        return weights;
    }

    public double getWeight(int i) {
        return weights[i];
    }

    public void setWeight(int i, double value) {
        weights[i] = value;
    }

    public void setFitness(int i) {
        fitness = i;
    }
}
