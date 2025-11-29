package com.example.k_mbti.service;

import java.util.List;

public class MbtiRuleService {

    public String estimateMbti(List<String> messages) {

        int e = 0, i = 0;
        int s = 0, n = 0;
        int t = 0, f = 0;
        int j = 0, p = 0;

        for (String msg : messages) {
            String m = msg.toLowerCase();

            // ---------------------------
            // E / I
            // ---------------------------
            if (m.contains("ㅋㅋ") || m.contains("ㅎㅎ") || m.contains("와")) {
                e++;
            }
            if (m.contains("음") || m.contains("...")) {
                i++;
            }

            // ---------------------------
            // S / N
            // ---------------------------
            if (m.matches(".*\\d+.*")) s++; // 숫자, 구체적
            if (m.contains("생각") || m.contains("느낌") || m.contains("아이디어")) n++;

            // ---------------------------
            // T / F
            // ---------------------------
            if (m.contains("왜") || m.contains("근데")) t++;
            if (m.contains("미안") || m.contains("고마워") || m.contains("ㅠ")) f++;

            // ---------------------------
            // J / P
            // ---------------------------
            if (m.contains("해야") || m.contains("정리")) j++;
            if (m.contains("일단") || m.contains("나중에")) p++;
        }

        String EI = (e >= i) ? "E" : "I";
        String SN = (s >= n) ? "S" : "N";
        String TF = (t >= f) ? "T" : "F";
        String JP = (j >= p) ? "J" : "P";

        return EI + SN + TF + JP;
    }
}
