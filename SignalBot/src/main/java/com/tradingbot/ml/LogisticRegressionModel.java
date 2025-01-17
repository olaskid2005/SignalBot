package com.tradingbot.ml;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a simple Logistic Regression model.
 */
public class LogisticRegressionModel extends MLModel {

    private double[] weights;
    private double bias;
    private final double learningRate;
    private final int epochs;

    /**
     * Constructor for LogisticRegressionModel.
     *
     * @param inputSize    The number of features in the input data.
     * @param learningRate The learning rate for gradient descent.
     * @param epochs       The number of training iterations.
     */
    public LogisticRegressionModel(int inputSize, double learningRate, int epochs) {
        if (inputSize <= 0 || learningRate <= 0 || epochs <= 0) {
            throw new IllegalArgumentException("Invalid parameters for Logistic Regression.");
        }
        this.weights = new double[inputSize];
        this.bias = 0;
        this.learningRate = learningRate;
        this.epochs = epochs;
    }

    @Override
    public void train(List<double[]> inputs, List<Double> targets) {
        if (inputs == null || targets == null || inputs.size() != targets.size()) {
            throw new IllegalArgumentException("Invalid training data.");
        }

        int dataSize = inputs.size();

        for (int epoch = 0; epoch < epochs; epoch++) {
            double totalLoss = 0;

            for (int i = 0; i < dataSize; i++) {
                double[] input = inputs.get(i);
                double target = targets.get(i);

                // Calculate prediction
                double linearOutput = bias;
                for (int j = 0; j < input.length; j++) {
                    linearOutput += input[j] * weights[j];
                }
                double prediction = sigmoid(linearOutput);

                // Calculate gradients
                double error = prediction - target;
                totalLoss += -target * Math.log(prediction) - (1 - target) * Math.log(1 - prediction);

                for (int j = 0; j < weights.length; j++) {
                    weights[j] -= learningRate * error * input[j];
                }
                bias -= learningRate * error;
            }

            // Print loss for debugging (optional)
            System.out.println("Epoch " + (epoch + 1) + ", Loss: " + totalLoss / dataSize);
        }
    }

    @Override
    public List<Double> predict(List<double[]> inputs) {
        List<Double> predictions = new ArrayList<>();

        for (double[] input : inputs) {
            double linearOutput = bias;
            for (int i = 0; i < input.length; i++) {
                linearOutput += input[i] * weights[i];
            }
            predictions.add(sigmoid(linearOutput));
        }

        return predictions;
    }

    @Override
    public double evaluate(List<double[]> inputs, List<Double> targets) {
        if (inputs == null || targets == null || inputs.size() != targets.size()) {
            throw new IllegalArgumentException("Invalid evaluation data.");
        }

        int correct = 0;
        for (int i = 0; i < inputs.size(); i++) {
            double prediction = predict(List.of(inputs.get(i))).get(0) >= 0.5 ? 1.0 : 0.0;
            if (prediction == targets.get(i)) {
                correct++;
            }
        }

        return (double) correct / targets.size();
    }

    /**
     * Sigmoid activation function.
     *
     * @param x The input value.
     * @return The sigmoid of x.
     */
    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
