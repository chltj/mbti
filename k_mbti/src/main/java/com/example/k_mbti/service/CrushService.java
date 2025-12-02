package com.example.k_mbti.service;

import com.example.k_mbti.dto.CrushResultDto;
import com.example.k_mbti.parser.KakaoParser;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CrushService {

    public CrushResultDto analyze(String text, String myName, String targetName, String range) {

        // 1) 카카오톡 텍스트를 유저별 메시지로 파싱
        Map<String, List<String>> userMessages = KakaoParser.parseByUser(text);

        // 내 메시지 / 상대 메시지 리스트
        List<String> myMessages = userMessages.getOrDefault(myName, Collections.emptyList());
        List<String> targetMessages = userMessages.getOrDefault(targetName, Collections.emptyList());

        int myCount = myMessages.size();
        int targetCount = targetMessages.size();
        int totalCount = userMessages.values().stream().mapToInt(List::size).sum();

        // 혹시라도 totalCount가 0이면(파싱 실패한 경우) 안전장치
        if (totalCount == 0) {
            CrushResultDto empty = new CrushResultDto();
            empty.setScore(0);
            empty.setLevel("분석 불가");
            empty.setComment("대화 내용을 제대로 읽지 못했어요. 파일 형식을 다시 확인해 주세요.");
            empty.setReplySpeedLabel("데이터 없음");
            empty.setFrequencyLabel("데이터 없음");
            empty.setInitiativeLabel("데이터 없음");
            empty.setWarmthLabel("데이터 없음");
            return empty;
        }

        // 2) 아주 간단한 지표들 계산 (일단 텍스트 기반)
        //    나중에 시간/응답속도 분석을 추가하면 더 정교해짐

        // 상대 말 비율 (상대가 대화에서 얼마나 많이 등장하는지)
        double targetRatio = (double) targetCount / totalCount;

        // warmth 점수: 상대 메시지 중 ㅋㅋ/ㅎㅎ/^^/이모티콘 비율
        int warmthCount = 0;
        for (String msg : targetMessages) {
            if (msg.contains("ㅋㅋ") || msg.contains("ㅎㅎ") ||
                msg.contains("^^")  || msg.contains("ㅠㅠ") ||
                msg.contains("😊")  || msg.contains("😍") ||
                msg.contains("😅")  || msg.contains("🙏")) {
                warmthCount++;
            }
        }
        double warmthRatio = targetMessages.isEmpty() ? 0.0 : (double) warmthCount / targetMessages.size();

        // 대화량 기준
        int totalLines = totalCount;

        // 3) 점수 만들기 (0~100, 임시 가중치 예시)
        int score = 0;

        // (1) 상대 등장 비율
        if (targetRatio >= 0.5)      score += 35;
        else if (targetRatio >= 0.3) score += 25;
        else if (targetRatio >= 0.15)score += 15;
        else                         score += 8;

        // (2) 대화량
        if (totalLines >= 300)       score += 30;
        else if (totalLines >= 150)  score += 22;
        else if (totalLines >= 50)   score += 15;
        else                         score += 8;

        // (3) 말투 따뜻함
        if (warmthRatio >= 0.4)      score += 35;
        else if (warmthRatio >= 0.2) score += 25;
        else if (warmthRatio >= 0.1) score += 15;
        else                         score += 8;

        if (score > 100) score = 100;

        // 4) 레벨 / 코멘트 / 라벨 만들기
        String level;
        String comment;
        if (score >= 80) {
            level = "관심 많음 / 썸 느낌 가득 💓";
            comment = "대화량과 말투를 보면 꽤 높은 호감도가 느껴져요. " +
                      "상대가 편하게 대화하고, 감정 표현도 자주 하는 편이에요.";
        } else if (score >= 60) {
            level = "호감 있음 / 친한 친구 이상 😊";
            comment = "서로 적당히 자주 연락하고, 말투도 비교적 부드러운 편이에요. " +
                      "친근한 관계로 보이고 호감도도 어느 정도 느껴져요.";
        } else if (score >= 40) {
            level = "보통 / 가벼운 친분 정도 🙂";
            comment = "필요할 때 연락하는 정도의 사이일 가능성이 커요. " +
                      "호감이 아주 없진 않지만, 아직 적극적으로 다가오는 느낌은 부족할 수 있어요.";
        } else {
            level = "낮음 / 아직은 거리감 있음 🥲";
            comment = "대화량이 적거나, 상대의 말투가 꽤 담백한 편이에요. " +
                      "지금은 일상적인 지인 수준의 관계일 수 있어요.";
        }

        // 라벨 텍스트들
        String frequencyLabel;
        if (totalLines >= 300) {
            frequencyLabel = "대화량 많음 (매우 자주 연락하는 편)";
        } else if (totalLines >= 150) {
            frequencyLabel = "대화량 보통 이상 (자주 연락하는 편)";
        } else if (totalLines >= 50) {
            frequencyLabel = "대화량 보통 (가끔 연락하는 편)";
        } else {
            frequencyLabel = "대화량 적음 (간헐적으로 연락)";
        }

        String initiativeLabel = String.format(
                "메시지 개수 - 나: %d, 상대: %d (상대 비율: %.0f%%)",
                myCount, targetCount, targetRatio * 100
        );

        String warmthLabel;
        if (warmthRatio >= 0.4) {
            warmthLabel = "이모티콘/ㅋㅋ/ㅎㅎ 많이 사용 (따뜻하고 편한 말투)";
        } else if (warmthRatio >= 0.2) {
            warmthLabel = "적당히 사용 (친근한 말투)";
        } else if (warmthRatio >= 0.1) {
            warmthLabel = "가끔 사용 (조금 담담한 말투)";
        } else {
            warmthLabel = "거의 사용하지 않음 (단정하고 담백한 말투)";
        }

        // 지금은 응답 속도 분석은 안 넣었으니 안내 문구만
        String replySpeedLabel = "※ 현재 버전은 응답 속도 대신 대화량/말투 기반으로만 계산합니다.";

        // 5) DTO에 담아서 리턴
        CrushResultDto dto = new CrushResultDto();
        dto.setScore(score);
        dto.setLevel(level);
        dto.setComment(comment);
        dto.setReplySpeedLabel(replySpeedLabel);
        dto.setFrequencyLabel(frequencyLabel);
        dto.setInitiativeLabel(initiativeLabel);
        dto.setWarmthLabel(warmthLabel);

        return dto;
    }
}
