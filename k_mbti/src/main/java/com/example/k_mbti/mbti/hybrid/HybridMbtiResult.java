package com.example.k_mbti.mbti.hybrid;

public class HybridMbtiResult {

    private String finalMbti;
    private double finalConfidence;
    private String ruleMbti;
    private String mlMbti;

    public HybridMbtiResult(String finalMbti,
                            double finalConfidence,
                            String ruleMbti,
                            String mlMbti) {
        this.finalMbti = finalMbti;
        this.finalConfidence = finalConfidence;
        this.ruleMbti = ruleMbti;
        this.mlMbti = mlMbti;
    }

    public String getFinalMbti() {
        return finalMbti;
    }

    public double getFinalConfidence() {
        return finalConfidence;
    }

    public String getRuleMbti() {
        return ruleMbti;
    }

    public String getMlMbti() {
        return mlMbti;
    }
}
