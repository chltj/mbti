package com.example.k_mbti.parser;

import com.example.k_mbti.dto.TalkData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KakaoParser {

    // 카카오톡 내보내기 파일의 표준 대화 패턴
    // 패턴 예: 2023. 12. 5. 오후 5:00, 김OO : 안녕하세요
    // 그룹: 1:날짜 부분, 2:오전/오후, 3:시간, 4:발화자, 5:내용
    private static final Pattern TALK_PATTERN = Pattern.compile(
        "^(\\d{4}\\.\\s+\\d{1,2}\\.\\s+\\d{1,2}\\.\\s+)(오전|오후)\\s+(\\d{1,2}:\\d{2}),\\s+(.+?)\\s+:\\s+(.*)$"
    );

    // 날짜 포맷: yyyy. M. d. a h:mm (12시간 형식에 맞춰서 사용)
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy. M. d. a h:mm");

    /**
     * 카카오톡 대화 텍스트를 파싱하여 TalkData 객체 리스트를 반환합니다.
     * CrushService에서 KakaoParser.parse(text, myName, targetName) 형태로 호출됩니다.
     */
    public static List<TalkData> parse(String text, String userA, String userB) {
        List<TalkData> talkList = new ArrayList<>();
        String[] lines = text.split("\\r?\\n"); // \r?\n으로 Windows/Linux 줄바꿈 모두 처리
        boolean isStartTalk = false;

        // BOM 제거
        if (text != null && text.startsWith("\uFEFF")) {
            text = text.substring(1);
        }

        for (String line : lines) {
            line = line.trim();
            
            // 대화 시작 지점 찾기
            if (line.endsWith("카카오톡 대화")) {
                isStartTalk = true;
                continue;
            }
            if (!isStartTalk) continue;
            
            if (line.isEmpty()) continue;

            Matcher matcher = TALK_PATTERN.matcher(line);

            if (matcher.matches()) {
                // 파싱된 그룹 추출
                String datePart = matcher.group(1).trim();
                String ampmPart = matcher.group(2);
                String timePart = matcher.group(3);
                String speaker = matcher.group(4).trim();
                String content = matcher.group(5).trim();

                // 대화 참여자만 포함
                if (!(speaker.equals(userA) || speaker.equals(userB))) {
                    continue;
                }
                
                try {
                    // 날짜+시간 문자열을 LocalDateTime으로 변환
                    String dateTimeStr = datePart + " " + ampmPart + " " + timePart;
                    LocalDateTime timestamp = LocalDateTime.parse(dateTimeStr, FORMATTER);

                    TalkData data = new TalkData();
                    data.setTimestamp(timestamp);
                    data.setSpeaker(speaker);
                    data.setContent(content);

                    // 시스템 메시지 필터링 (TalkData 내부 로직 사용)
                    if (!data.isNonTalk()) {
                        talkList.add(data);
                    }
                } catch (DateTimeParseException e) {
                    // 파싱 오류 발생 시 해당 라인 건너뛰기
                }
            }
        }
        return talkList;
    }
}