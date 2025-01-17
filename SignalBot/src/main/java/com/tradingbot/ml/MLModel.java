package com.tradingbot.ml;

import java.util.List;

/**
 * Abstract base class for machine learning models.
 */
public abstract class MLModel {

    /**
     * Trains the model using the provided input and target data.
     *
     * @param inputs  The training inputs.
     * @param targets The training targets.
     */
    public abstract void train(List<double[]> inputs, List<Double> targets);

    /**
     * Makes predictions using the trained model on the given inputs.
     *
     * @param inputs The inputs for which predictions are required.
     * @return A list of predictions corresponding to the inputs.
     */
    public abstract List<Double> predict(List<double[]> inputs);

    /**
     * Evaluates the model's performance using test data.
     *
     * @param inputs  The test inputs.
     * @param targets The true targets for the test inputs.
     * @return The evaluation score (e.g., accuracy, RMSE).
     */
    public abstract double evaluate(List<double[]> inputs, List<Double> targets);
}
