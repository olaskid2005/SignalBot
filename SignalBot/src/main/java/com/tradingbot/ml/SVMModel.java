package com.tradingbot.ml;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a simple Support Vector Machine (SVM) model for classification.
 */
public class SVMModel extends MLModel {

    private double[] weights;
    private double bias;
    private final double learningRate;
    private final int epochs;
    private final double regularization;

    /**
     * Constructor for SVMModel.
     *
     * @param inputSize      The number of features in the input data.
     * @param learningRate   The learning rate for gradient descent.
     * @param epochs         The number of training iterations.
     * @param regularization The regularization parameter to control overfitting.
     */
    public SVMModel(int inputSize, double learningRate, int epochs, double regularization) {
        if (inputSize <= 0 || learningRate <= 0 || epochs <= 0 || regularization < 0) {
            throw new IllegalArgumentException("Invalid parameters for SVM.");
        }
        this.weights = new double[inputSize];
        this.bias = 0;
        this.learningRate = learningRate;
        this.epochs = epochs;
        this.regularization = regularization;
    }

    @Override
    public void train(List<double[]> inputs, List<Double> targets) {
        if (inputs == null || targets == null || inputs.size() != targets.size()) {
            throw new IllegalArgumentException("Invalid training data.");
        }

        int dataSize = inputs.size();

        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < dataSize; i++) {
                double[] input = inputs.get(i);
                double target = targets.get(i) == 1.0 ? 1.0 : -1.0; // Convert target to +1 or -1

                // Calculate margin
                double margin = target * (dotProduct(weights, input) + bias);

                if (margin < 1) {
                    // Update weights and bias with regularization
                    for (int j = 0; j < weights.length; j++) {
                        weights[j] += learningRate * (target * input[j] - 2 * regularization * weights[j]);
                    }
                    bias += learningRate * target;
                } else {
                    // Apply regularization only
                    for (int j = 0; j < weights.length; j++) {
                        weights[j] -= learningRate * 2 * regularization * weights[j];
                    }
                }
            }
        }
    }

    @Override
    public List<Double> predict(List<double[]> inputs) {
        List<Double> predictions = new ArrayList<>();

        for (double[] input : inputs) {
            double output = dotProduct(weights, input) + bias;
            predictions.add(output >= 0 ? 1.0 : 0.0);
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
            double prediction = predict(List.of(inputs.get(i))).get(0);
            if (prediction == targets.get(i)) {
                correct++;
            }
        }

        return (double) correct / targets.size();
    }

    /**
     * Calculates the dot product of two vectors.
     *
     * @param vector1 The first vector.
     * @param vector2 The second vector.
     * @return The dot product of the two vectors.
     */
    private double dotProduct(double[] vector1, double[] vector2) {
        double result = 0;
        for (int i = 0; i < vector1.length; i++) {
            result += vector1[i] * vector2[i];
        }
        return result;
    }
}
