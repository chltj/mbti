package com.example.k_mbti.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap; // ⭐️ 오류 해결을 위해 추가
import java.util.List;
import java.util.Map; // ⭐️ 오류 해결을 위해 추가

@Service
public class MbtiRuleService {

    /**
     * 모든 참여자의 MBTI를 분석하고 결과를 Map<참여자 이름, MBTI> 형태로 반환합니다.
     * 이 메서드는 컨트롤러에서 호출됩니다.
     * @param talkDataByUser Map<참여자 이름, 발화 내용 리스트>
     * @return Map<참여자 이름, 분석된 MBTI 유형 (String)>
     */
    public Map<String, String> analyzeAllUsers(Map<String, List<String>> talkDataByUser) {
        Map<String, String> allResults = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : talkDataByUser.entrySet()) {
            String speakerName = entry.getKey();
            List<String> messages = entry.getValue();
            
            // 발화 메시지가 너무 적은 참여자는 분석에서 제외
            if (messages.size() < 10) { 
                allResults.put(speakerName, "분석 불가 (발화량 부족)");
                continue;
            }

            // 강화된 estimateMbti 메서드를 사용하여 MBTI 분석
            String mbtiType = estimateMbti(messages);

            // 결과를 Map에 저장
            allResults.put(speakerName, mbtiType);
        }
        return allResults;
    }

    /**
     * 한 사용자의 메시지 리스트를 기반으로 MBTI를 추정합니다.
     * 규칙을 세분화하고 가중치를 적용하여 분석을 강화합니다.
     */
    public String estimateMbti(List<String> messages) {

        int eiScore = 0; // E: +, I: -
        int snScore = 0; // S: +, N: -
        int tfScore = 0; // T: +, F: -
        int jpScore = 0; // J: +, P: -
        
        long totalLength = messages.stream().mapToLong(String::length).sum();
        double avgLength = (messages.size() > 0) ? (double) totalLength / messages.size() : 0;
        int totalMessages = messages.size();
        
        // ----------------------------------------------------
        // ⭐️ 1. 양적 데이터 기반 규칙 (E/I, J/P 보강) ⭐️
        // ----------------------------------------------------
        
        // E 성향: 총 발화량이 많음 (+ 가중치 3)
        if (totalMessages > 500) {
            eiScore += 3;
        } 
        // I 성향: 평균 메시지 길이가 길면 숙고하여 장문을 보낸다고 가정 (- 가중치 2)
        if (avgLength > 80) {
            eiScore -= 2;
        }
        // P 성향: 평균 메시지 길이가 짧으면 단답형으로 빠르게 반응한다고 가정 (- 가중치 1)
        if (avgLength < 25) {
            jpScore -= 1; 
        }

        // ----------------------------------------------------
        // ⭐️ 2. 내용 및 키워드 기반 규칙 (세부 가중치 적용) ⭐️
        // ----------------------------------------------------
        
        for (String msg : messages) {
            String m = msg.toLowerCase();
            int weight = 1;

            // ---------------------------
            // E (+) / I (-) (활발성 vs 숙고)
            // ---------------------------
            // E 강화: 이모티콘 반복, 감탄사
            if (m.contains("ㅋㅋ") || m.contains("ㅎㅎ") || m.contains("와") || m.contains("대박")) {
                eiScore += weight * 2; 
            }
            // I 강화: 침묵, 간결한 종결, 사색
            if (m.contains("음") || m.contains("...") || m.contains("생각 중")) {
                eiScore -= weight * 2; 
            }
            
            // ---------------------------
            // S (+) / N (-) (현실 vs 추상)
            // ---------------------------
            // S 강화: 시간, 장소, 구체적 데이터 요청 및 제공
            if (m.matches(".*\\d+.*") || m.contains("몇 시") || m.contains("어디서") || m.contains("얼마나")) {
                snScore += weight * 2; 
            }
            // N 강화: 추측, 비유, 비현실적 주제
            if (m.contains("생각") || m.contains("느낌") || m.contains("아이디어") || m.contains("어쩌면") || m.contains("상상")) {
                snScore -= weight * 2;
            }

            // ---------------------------
            // T (+) / F (-) (논리 vs 감정)
            // ---------------------------
            // T 강화: 원인, 결과, 효율, 비판, 결론 유도
            if (m.contains("왜") || m.contains("근데") || m.contains("결론은") || m.contains("효율")) {
                tfScore += weight * 2;
            }
            // F 강화: 공감, 위로, 사과, 감정 표현
            if (m.contains("미안") || m.contains("고마워") || m.contains("ㅠ") || m.contains("힘내") || m.contains("괜찮아?")) {
                tfScore -= weight * 2;
            }
            // T 강화: 단답형 '왜' (논리적 반문)
            if (m.contains("왜") && m.length() < 15) {
                tfScore += weight * 1;
            }

            // ---------------------------
            // J (+) / P (-) (계획 vs 유동성)
            // ---------------------------
            // J 강화: 마감일, 정리, 계획 확정 요청
            if (m.contains("해야") || m.contains("정리") || m.contains("계획") || m.contains("까지")) {
                jpScore += weight * 2;
            }
            // P 강화: 즉흥, 회피, 미루기
            if (m.contains("일단") || m.contains("나중에") || m.contains("대충") || m.contains("상관 없어")) {
                jpScore -= weight * 2;
            }
        }
        
        // ----------------------------------------------------
        // ⭐️ 3. 최종 MBTI 유형 결정 ⭐️
        // ----------------------------------------------------

        String EI = (eiScore >= 0) ? "E" : "I";
        String SN = (snScore >= 0) ? "S" : "N";
        String TF = (tfScore >= 0) ? "T" : "F";
        String JP = (jpScore >= 0) ? "J" : "P";

        return EI + SN + TF + JP;
    }
}