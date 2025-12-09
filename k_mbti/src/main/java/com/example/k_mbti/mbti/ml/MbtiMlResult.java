package com.example.k_mbti.mbti.ml;

public class MbtiMlResult {

    private String mbti;
    private double probability;

    public MbtiMlResult(String mbti, double probability) {
        this.mbti = mbti;
        this.probability = probability;
    }

    public String getMbti() {
        return mbti;
    }

    public double getProbability() {
        return probability;
    }
}
