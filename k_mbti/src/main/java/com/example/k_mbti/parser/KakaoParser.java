package com.example.k_mbti.parser;

import com.example.k_mbti.dto.TalkData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Map import는 필요 없으므로 제거 (import java.util.Map;)

public class KakaoParser {

    // 카카오톡 내보내기 파일의 표준 대화 패턴 (역슬래시 이스케이프 적용 완료)
    // 🚨 수정: \d, \s 앞에 \를 추가했습니다.
// KakaoParser.java 파일 내부

// ... (기존 parse 메소드 정의 후) ...

// MBTI 분석용: 시간 정보 없이 대화 내용만 추출하여 Map으로 반환
public static Map<String, List<String>> parseByUser(String rawText) {
    Map<String, List<String>> result = new HashMap<>();
    
    // 카카오톡 파일의 표준 대화 패턴
    // (시간 정보는 있으나 사용하지 않고 발화자와 내용만 추출함)
    Pattern SIMPLE_TALK_PATTERN = Pattern.compile(
        "^\\d{4}\\.\\s*\\d{1,2}\\.\\s*\\d{1,2}\\.\\s*(오전|오후)\\s+\\d{1,2}:\\d{2},\\s*(.+?)\\s*:\\s*(.*)$"
    );

    String[] lines = rawText.split("\\r?\\n");
    boolean isStartTalk = false;

    // BOM 제거
    if (rawText != null && rawText.startsWith("\uFEFF")) {
        rawText = rawText.substring(1);
    }

    for (String line : lines) {
        if (line.endsWith("카카오톡 대화")) {
            isStartTalk = true;
            continue;
        }
        if (!isStartTalk) continue;
        
        Matcher matcher = SIMPLE_TALK_PATTERN.matcher(line.trim());
        
        if (matcher.matches()) {
            // 2: 발화자, 3: 내용
            String speaker = matcher.group(2).trim();
            String content = matcher.group(3).trim();

            // 시스템 메시지 필터링 및 내용이 비어있지 않은지 확인
            if (!(content.startsWith("[") && content.endsWith("]")) && !content.isEmpty()) {
                result.computeIfAbsent(speaker, k -> new ArrayList<>()).add(content);
            }
        }
    }
    return result;
}

public static List<TalkData> parse(String text, String myName, String targetName) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'parse'");
}

    // 🚨 parseByUser 메소드는 CrushService에서 사용되지 않으므로 제거합니다.
    // 만약 MBTI 분석을 위해 필요하다면 MainController의 수정 단계에서 구현체를 사용해야 합니다.
}