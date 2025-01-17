package com.tradingbot.ml;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implements a basic Neural Network model for classification.
 */
public class NeuralNetworkModel extends MLModel {

    private final int inputSize;
    private final int hiddenSize;
    private final int outputSize;
    private final double learningRate;
    private final int epochs;

    private double[][] weightsInputHidden;
    private double[][] weightsHiddenOutput;
    private double[] biasHidden;
    private double[] biasOutput;

    /**
     * Constructor for NeuralNetworkModel.
     *
     * @param inputSize    Number of input features.
     * @param hiddenSize   Number of neurons in the hidden layer.
     * @param outputSize   Number of output neurons.
     * @param learningRate Learning rate for gradient descent.
     * @param epochs       Number of training iterations.
     */
    public NeuralNetworkModel(int inputSize, int hiddenSize, int outputSize, double learningRate, int epochs) {
        if (inputSize <= 0 || hiddenSize <= 0 || outputSize <= 0 || learningRate <= 0 || epochs <= 0) {
            throw new IllegalArgumentException("Invalid parameters for Neural Network.");
        }

        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.learningRate = learningRate;
        this.epochs = epochs;

        initializeWeightsAndBiases();
    }

    private void initializeWeightsAndBiases() {
        Random random = new Random();
        weightsInputHidden = new double[inputSize][hiddenSize];
        weightsHiddenOutput = new double[hiddenSize][outputSize];
        biasHidden = new double[hiddenSize];
        biasOutput = new double[outputSize];

        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightsInputHidden[i][j] = random.nextGaussian() * 0.01;
            }
        }

        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weightsHiddenOutput[i][j] = random.nextGaussian() * 0.01;
            }
        }
    }

    @Override
public void train(List<double[]> inputList, List<Double> targetList) {
    if (inputList == null || targetList == null || inputList.size() != targetList.size()) {
        throw new IllegalArgumentException("Invalid training data.");
    }

    for (int epoch = 0; epoch < epochs; epoch++) {
        double totalLoss = 0;

        for (int i = 0; i < inputList.size(); i++) {
            double[] input = inputList.get(i);
            double target = targetList.get(i);

            // Forward pass
            double[] hiddenLayer = activate(add(dot(input, weightsInputHidden), biasHidden));
            double[] outputLayer = activate(add(dot(hiddenLayer, weightsHiddenOutput), biasOutput));

            // Compute loss
            double loss = Math.pow(outputLayer[0] - target, 2) / 2;
            totalLoss += loss;

            // Backward pass
            double[] outputError = new double[outputSize];
            for (int j = 0; j < outputSize; j++) {
                outputError[j] = (outputLayer[j] - target) * outputLayer[j] * (1 - outputLayer[j]);
            }

            double[] hiddenError = new double[hiddenSize];
            for (int j = 0; j < hiddenSize; j++) {
                double sum = 0;
                for (int k = 0; k < outputSize; k++) {
                    sum += outputError[k] * weightsHiddenOutput[j][k];
                }
                hiddenError[j] = sum * hiddenLayer[j] * (1 - hiddenLayer[j]);
            }

            // Update weights and biases for hidden-to-output layer
            for (int j = 0; j < hiddenSize; j++) {
                for (int k = 0; k < outputSize; k++) {
                    weightsHiddenOutput[j][k] -= learningRate * outputError[k] * hiddenLayer[j];
                }
            }
            for (int k = 0; k < outputSize; k++) {
                biasOutput[k] -= learningRate * outputError[k];
            }

            // Update weights and biases for input-to-hidden layer
            for (int j = 0; j < inputSize; j++) {
                for (int k = 0; k < hiddenSize; k++) {
                    weightsInputHidden[j][k] -= learningRate * hiddenError[k] * input[j];
                }
            }
            for (int j = 0; j < hiddenSize; j++) {
                biasHidden[j] -= learningRate * hiddenError[j]; // Fix: Use `j` for hidden layer neurons
            }
        }
        System.out.println("Epoch " + (epoch + 1) + ", Loss: " + totalLoss / inputList.size());
    }
}


    @Override
    public List<Double> predict(List<double[]> inputList) {
        List<Double> predictions = new ArrayList<>();
        for (double[] input : inputList) {
            double[] hiddenLayer = activate(add(dot(input, weightsInputHidden), biasHidden));
            double[] outputLayer = activate(add(dot(hiddenLayer, weightsHiddenOutput), biasOutput));
            predictions.add(outputLayer[0]);
        }
        return predictions;
    }

    @Override
    public double evaluate(List<double[]> inputList, List<Double> targetList) {
        if (inputList == null || targetList == null || inputList.size() != targetList.size()) {
            throw new IllegalArgumentException("Invalid evaluation data.");
        }

        double totalLoss = 0;
        for (int i = 0; i < inputList.size(); i++) {
            double prediction = predict(List.of(inputList.get(i))).get(0);
            totalLoss += Math.pow(prediction - targetList.get(i), 2) / 2;
        }

        return totalLoss / inputList.size();
    }

    private double[] dot(double[] vector, double[][] matrix) {
        double[] result = new double[matrix[0].length];
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < vector.length; j++) {
                result[i] += vector[j] * matrix[j][i];
            }
        }
        return result;
    }

    private double[] add(double[] vector, double[] bias) {
        double[] result = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            result[i] = vector[i] + bias[i];
        }
        return result;
    }

    private double[] activate(double[] vector) {
        double[] result = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            result[i] = 1 / (1 + Math.exp(-vector[i]));
        }
        return result;
    }
}
