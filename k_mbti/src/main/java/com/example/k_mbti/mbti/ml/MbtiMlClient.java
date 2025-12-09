package com.example.k_mbti.mbti.ml;

import org.springframework.stereotype.Component;

@Component
public class MbtiMlClient {

    public MbtiMlResult predict(String fullText) {
        String predictedMbti = "ESTP";
        double confidence = 0.82;
        return new MbtiMlResult(predictedMbti, confidence);
    }
}
