package de.raphaelmuesseler.neuralnetwork;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class NeuralNetworkTest {
    private static double[][] XOR_INPUT = {{0.0, 0.0}, {1.0, 0.0}, {0.0, 1.0}, {1.0, 1.0}};
    private static double[][] XOR_OUTPUT = {{0.0}, {1.0}, {1.0}, {0.0}};

    public static void main(String[] args) {
        BasicNetwork network = new BasicNetwork();

        network.addLayer(new BasicLayer(null, true, 2));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 3));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));

        network.getStructure().finalizeStructure();
        network.reset();

        MLDataSet trainingsSet = new BasicMLDataSet(XOR_INPUT, XOR_OUTPUT);

        final ResilientPropagation train = new ResilientPropagation(network, trainingsSet);
        int epoch = 1;

        do {
            train.iteration();
            System.out.println("Epoch #" + epoch + "; Error: " + train.getError());
            epoch++;
        } while (train.getError() > 0.01);

        train.finishTraining();

        System.out.println("Neural network results: ");
        for (MLDataPair pair : trainingsSet) {
            final MLData output = network.compute(pair.getInput());
            System.out.println(pair.getIdeal().getData(0) + ", " + pair.getInput().getData(1) +
                    ", actual: " + output.getData(0) + ", ideal: " + pair.getIdeal().getData(0));
        }

        Encog.getInstance().shutdown();
    }
}
