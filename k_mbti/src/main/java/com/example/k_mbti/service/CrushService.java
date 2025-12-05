package com.example.k_mbti.service;

import com.example.k_mbti.dto.CrushResultDto;
import com.example.k_mbti.dto.TalkData;
import com.example.k_mbti.parser.KakaoParser;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CrushService {

    // 점수 가중치 정의 (총합 100점)
    private static final int WEIGHT_RESPONSE = 30;   // 답장 속도
    private static final int WEIGHT_INITIATIVE = 15; // 질문 횟수
    private static final int WEIGHT_WARMTH = 15;     // 따뜻한 말투 (이모티콘 등)
    private static final int WEIGHT_FREQUENCY = 10;  // 대화 빈도 (총량)
    private static final int WEIGHT_RATIO = 10;      // 발화 비율
    private static final int WEIGHT_LENGTH = 10;     // 평균 메시지 길이 (성실도)
    private static final int WEIGHT_OVERLAP = 10;    // 활동 시간대 일치도

    /**
     * 카카오톡 대화 텍스트를 분석하여 호감도 결과를 반환합니다.
     */
    public CrushResultDto analyze(String text, String myName, String targetName, String range) {
        
        List<TalkData> talkList = KakaoParser.parse(text, myName, targetName);
        if (talkList.size() < 20) { // 최소 대화량 기준 상향 (20건)
            return buildLowVolumeResult(); 
        }

        Map<String, List<TalkData>> talkMap = talkList.stream()
                .collect(Collectors.groupingBy(TalkData::getSpeaker));
        
        List<TalkData> talksMe = talkMap.getOrDefault(myName, List.of());
        List<TalkData> talksTarget = talkMap.getOrDefault(targetName, List.of());
        
        // 3. 점수 항목별 계산 (최대 1.0)
        double responseFactor = calculateResponseScore(talksMe, talksTarget);
        double initiativeFactor = calculateInitiativeScore(talksTarget); 
        double warmthFactor = calculateWarmthScore(talksTarget);         
        double frequencyFactor = calculateFrequencyScore(talkList.size());
        double ratioFactor = calculateRatioScore(talksMe.size(), talksTarget.size());
        double lengthFactor = calculateLengthScore(talksTarget); 
        double overlapFactor = calculateOverlapScore(talksMe, talksTarget); 

        // 4. 최종 점수 합산 및 정규화
        int finalScore = (int) Math.round(
            (responseFactor * WEIGHT_RESPONSE) +
            (initiativeFactor * WEIGHT_INITIATIVE) +
            (warmthFactor * WEIGHT_WARMTH) +
            (frequencyFactor * WEIGHT_FREQUENCY) +
            (ratioFactor * WEIGHT_RATIO) +
            (lengthFactor * WEIGHT_LENGTH) +
            (overlapFactor * WEIGHT_OVERLAP)
        );
        finalScore = Math.min(Math.max(finalScore, 1), 100); 
        
        // 5. 결과 DTO 생성
        return buildResultDto(
            finalScore, responseFactor, initiativeFactor, warmthFactor,
            frequencyFactor, ratioFactor, lengthFactor, overlapFactor
        );
    }
    
    // ----------------------------------------------------------------------
    // [항목별 점수 계산 로직]
    // ----------------------------------------------------------------------

    private double calculateLengthScore(List<TalkData> talksTarget) {
        if (talksTarget.isEmpty()) return 0.0;
        double avgLength = talksTarget.stream()
                .filter(t -> !t.isEmoticon())
                .mapToInt(t -> t.getContent().length())
                .average()
                .orElse(0.0);
        if (avgLength >= 30) return 1.0;
        if (avgLength >= 15) return 0.7;
        if (avgLength >= 5) return 0.3;
        return 0.1;
    }

    private double calculateOverlapScore(List<TalkData> talksMe, List<TalkData> talksTarget) {
        if (talksMe.isEmpty() || talksTarget.isEmpty()) return 0.0;

        Map<Integer, Long> meActivity = talksMe.stream()
                .collect(Collectors.groupingBy(t -> t.getTimestamp().getHour(), Collectors.counting()));
        Map<Integer, Long> targetActivity = talksTarget.stream()
                .collect(Collectors.groupingBy(t -> t.getTimestamp().getHour(), Collectors.counting()));

        long totalOverlapCount = 0;
        long totalCount = 0;

        for (int hour = 0; hour < 24; hour++) {
            long meCount = meActivity.getOrDefault(hour, 0L);
            long targetCount = targetActivity.getOrDefault(hour, 0L);
            totalOverlapCount += Math.min(meCount, targetCount);
            totalCount += meCount + targetCount; 
        }

        if (totalCount == 0) return 0.0;
        
        double overlapRatio = (double) totalOverlapCount * 2 / totalCount;
        return Math.min(overlapRatio, 1.0);
    }

    private double calculateResponseScore(List<TalkData> talksMe, List<TalkData> talksTarget) {
        long totalResponseTimeSec = 0;
        int responseCount = 0;
        for (TalkData myTalk : talksMe) {
            Duration duration = Duration.between(myTalk.getTimestamp(), talksTarget.stream()
                .filter(t -> t.getTimestamp().isAfter(myTalk.getTimestamp()))
                .map(TalkData::getTimestamp)
                .min(java.util.Comparator.naturalOrder())
                .orElse(myTalk.getTimestamp().plusDays(1)));
            if (duration.getSeconds() < 86400) {
                totalResponseTimeSec += duration.getSeconds();
                responseCount++;
            }
        }
        if (responseCount == 0) return 0.1; 
        double avgResponseTimeSec = (double) totalResponseTimeSec / responseCount;
        if (avgResponseTimeSec < 10) return 1.0;
        if (avgResponseTimeSec < 60) return 0.8;
        if (avgResponseTimeSec < 300) return 0.5;
        if (avgResponseTimeSec < 1800) return 0.2; 
        return 0.1;
    }

    private double calculateInitiativeScore(List<TalkData> talksTarget) {
        if (talksTarget.isEmpty()) return 0.0;
        long targetQuestionCount = talksTarget.stream().filter(TalkData::isQuestion).count();
        double score = (double) targetQuestionCount / (talksTarget.size() / 20.0);
        return Math.min(score, 1.0);
    }
    
    private double calculateWarmthScore(List<TalkData> talksTarget) {
        if (talksTarget.isEmpty()) return 0.0;
        long emoticonCount = talksTarget.stream().filter(TalkData::isEmoticon).count();
        long smileCount = talksTarget.stream()
            .filter(t -> t.getContent().contains("ㅋㅋ") || t.getContent().contains("ㅎㅎ")).count();
        double targetMessagesSize = (double) talksTarget.size();
        double score = (emoticonCount + smileCount) / (targetMessagesSize / 5.0); 
        return Math.min(score, 1.0);
    }

    private double calculateFrequencyScore(int totalTalks) {
        if (totalTalks >= 500) return 1.0;
        if (totalTalks >= 200) return 0.7;
        if (totalTalks >= 50) return 0.4;
        return 0.1;
    }
    
    private double calculateRatioScore(int talksMeSize, int talksTargetSize) {
        double totalTalks = talksMeSize + talksTargetSize;
        if (totalTalks == 0) return 0.0;
        double targetRatio = talksTargetSize / totalTalks;
        if (targetRatio >= 0.7) return 1.0;
        if (targetRatio >= 0.5) return 0.8;
        if (targetRatio >= 0.3) return 0.5;
        return 0.2;
    }

    /**
     * 최종 결과 DTO 생성 및 라벨링
     */
    private CrushResultDto buildResultDto(
            int finalScore, double responseFactor, double initiativeFactor, 
            double warmthFactor, double frequencyFactor, double ratioFactor,
            double lengthFactor, double overlapFactor) {
        
        String level;
        String comment;
        if (finalScore >= 80) { level = "🔥 불타는 호감도 (80%~100%)"; comment = "모든 지표에서 최고점을 기록! 높은 호감이 확실합니다."; } 
        else if (finalScore >= 60) { level = "💚 그린라이트 (60%~79%)"; comment = "응답 속도가 빠르고, 대화 성실도도 높습니다. 긍정적인 신호입니다."; } 
        else if (finalScore >= 40) { level = "💛 보통 수준 (40%~59%)"; comment = "친밀하지만, 아직은 적극적인 호감 표현이 부족할 수 있습니다."; } 
        else { level = "💙 낮은 수준 (1%~39%)"; comment = "대화의 양이나 질적인 측면에서 호감 신호가 약합니다."; }
        
        return new CrushResultDto(
            finalScore, level, comment,
            getReplySpeedLabel(responseFactor),
            getFrequencyLabel(frequencyFactor),
            getInitiativeLabel(initiativeFactor),
            getWarmthLabel(warmthFactor),
            getLengthLabel(lengthFactor),
            getOverlapLabel(overlapFactor),
            getRatioLabel(ratioFactor)
        );
    }
    
    /**
     * 대화량 부족 시 결과 (10개 인자)
     */
    private CrushResultDto buildLowVolumeResult() {
        return new CrushResultDto(1, "분석 불가", "대화량이 부족하여 분석이 어렵습니다.",
            "정보 없음", "정보 없음", "정보 없음", "정보 없음", "정보 없음", "정보 없음", "정보 없음");
    }
    
    // ----------------------------------------------------------------------
    // [세부 항목별 라벨링 메소드 - 본체 채움]
    // ----------------------------------------------------------------------
    
    private String getReplySpeedLabel(double factor) {
        if (factor >= 0.8) return "⚡ 즉답 수준: 대화에 매우 집중하고 있어요! (평균 1분 이내)";
        if (factor >= 0.5) return "✅ 빠른 편: 대화가 끊기지 않게 바로 답장해요. (평균 5분 이내)";
        if (factor >= 0.2) return "⌚ 보통: 적절한 시간 내에 답장하는 편이에요. (평균 30분 이내)";
        return "🐌 느린 편: 답장에 시간이 걸리는 편이에요.";
    }

    private String getFrequencyLabel(double factor) {
        if (factor >= 0.7) return "📈 매우 높음: 총 200건 이상 대화 기록";
        if (factor >= 0.4) return "⚖ 보통 이상: 총 50건 이상 대화 기록";
        return "📉 낮음: 대화량이 적은 편이에요.";
    }

    private String getInitiativeLabel(double factor) {
        if (factor >= 0.7) return "💬 적극적: 대화 20건당 질문 1회 이상 (당신에게 관심 많음)";
        if (factor >= 0.3) return "❓ 보통: 질문을 가끔 던지며 대화를 이어가요.";
        return "🙅‍♂️ 수동적: 주로 당신의 이야기에 반응하는 대화예요.";
    }

    private String getWarmthLabel(double factor) {
        if (factor >= 0.7) return "🥰 풍부한 감정: 이모티콘, 'ㅋㅋ/ㅎㅎ' 사용 빈도가 높아요.";
        if (factor >= 0.4) return "😊 보통: 적절한 감정 표현을 사용하여 친근감을 보여줘요.";
        return "😐 무미건조: 텍스트 위주 대화로 감정 표현이 적은 편이에요.";
    }

    private String getRatioLabel(double factor) {
        if (factor >= 0.7) return "높음: 상대 메시지 비율이 70% 이상";
        if (factor >= 0.5) return "균형적: 상대 메시지 비율이 50% 내외";
        if (factor >= 0.3) return "낮음: 상대 메시지 비율이 30% 내외";
        return "매우 낮음: 주로 당신이 대화를 주도해요.";
    }

    private String getLengthLabel(double factor) {
        if (factor >= 0.7) return "📝 성실함: 평균 메시지 길이가 긴 편이에요.";
        if (factor >= 0.3) return "⚖ 보통: 적절한 길이의 메시지 교환";
        return "📉 단답형: 짧고 간단한 형식적인 메시지가 많아요.";
    }

    private String getOverlapLabel(double factor) {
        if (factor >= 0.6) return "☀️ 일치함: 주로 비슷한 시간대에 활동하고 있어요.";
        if (factor >= 0.4) return "⌚ 보통: 활동 시간대가 어느 정도 겹쳐요.";
        return "🌙 불일치: 활동 시간대가 많이 달라요.";
    }
}