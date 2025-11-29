package com.example.k_mbti.parser;

import java.util.*;
import java.util.regex.*;

public class KakaoParser {

    // [이름] [오전 12:01] 메시지
    private static final Pattern CHAT_PATTERN =
            Pattern.compile("^\\[(.+?)\\]\\s*\\[(.+?)\\]\\s*(.*)$");

    public static Map<String, List<String>> parseByUser(String rawText) {
        Map<String, List<String>> result = new HashMap<>();

        // 혹시 BOM 같은 거 들어있으면 제거
        if (rawText != null && rawText.startsWith("\uFEFF")) {
            rawText = rawText.substring(1);
        }

        String[] lines = rawText.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // 헤더/구분선/메타 정보는 스킵
            if (line.contains("카카오톡 대화")) continue;
            if (line.startsWith("저장한 날짜")) continue;
            if (line.startsWith("---------------")) continue;

            Matcher m = CHAT_PATTERN.matcher(line);
            if (m.matches()) {
                String user = m.group(1).trim();   // [이름]
                // String time = m.group(2).trim(); // [오전 12:01] (지금은 안 씀)
                String message = m.group(3).trim();

                if (!message.isEmpty()) {
                    result.computeIfAbsent(user, k -> new ArrayList<>())
                          .add(message);
                }
            }
        }

        return result;
    }
}
