package com.example.k_mbti.mbti.hybrid;

import com.example.k_mbti.mbti.ml.MbtiMlClient;
import com.example.k_mbti.mbti.ml.MbtiMlResult;
import org.springframework.stereotype.Service;

@Service
public class MbtiHybridService {

    private final MbtiMlClient mlClient;

    public MbtiHybridService(MbtiMlClient mlClient) {
        this.mlClient = mlClient;
    }

    public HybridMbtiResult merge(String ruleMbti,
                                  double ruleScore,
                                  String fullText) {

        MbtiMlResult mlResult = mlClient.predict(fullText);

        String finalMbti;
        double finalScore;

        if (ruleMbti.equals(mlResult.getMbti())) {
            finalMbti = ruleMbti;
            finalScore = (ruleScore * 0.4) + (mlResult.getProbability() * 0.6);
        } else {
            finalMbti = mlResult.getMbti();
            finalScore = mlResult.getProbability();
        }

        return new HybridMbtiResult(
                finalMbti,
                finalScore,
                ruleMbti,
                mlResult.getMbti()
        );
    }
}
