package com.ac.common.interview;

import java.util.Random;

import org.junit.jupiter.api.Test;

public class WeightedFruitPickerTest {

    @Test
    public void test() {
        String[] fruits = {"Apple", "Banana", "Cherry", "Orange"};
        double[] probabilities = {0.5, 0.2, 0.2, 0.1}; // Apple is most likely to be picked

        WeightedFruitPicker picker = new WeightedFruitPicker(fruits, probabilities);

        // Generate random fruit picks
        for (int i = 0; i < 10; i++) {
            System.out.println(picker.pickRandomFruit());
        }
    }
}

 class WeightedFruitPicker {
    private final String[] fruits;
    private final double[] cumulativeProbabilities;
    private final Random random;

    public WeightedFruitPicker(String[] fruits, double[] probabilities) {
        this.fruits = fruits;
        cumulativeProbabilities = new double[probabilities.length];
        double sum = 0.0;

        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            cumulativeProbabilities[i] = sum;
        }

        random = new Random();
    }

    public String pickRandomFruit() {
        double rand = random.nextDouble() * cumulativeProbabilities[cumulativeProbabilities.length - 1];
        for (int i = 0; i < cumulativeProbabilities.length; i++) {
            if (rand <= cumulativeProbabilities[i]) {
                return fruits[i];
            }
        }
        return null; // Should never happen if probabilities are valid
    }

    
}
