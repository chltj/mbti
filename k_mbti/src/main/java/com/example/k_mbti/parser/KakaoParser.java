package com.example.k_mbti.parser;

import com.example.k_mbti.dto.TalkData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class KakaoParser {

    // 형식 A 예시:

    private static final Pattern TALK_LINE_OLD = Pattern.compile(
            "^(\\d{4})\\.\\s*(\\d{1,2})\\.\\s*(\\d{1,2})\\.\\s*" +   // yyyy. M. d.
            "(오전|오후)\\s+" +                                      // 오전/오후
            "(\\d{1,2}):(\\d{2}),\\s*" +                             // h:mm,
            "(.+?)\\s*:\\s*(.*)$"                                    // 이름 : 내용
    );

    // 형식 B 날짜 구분선 예시:
    // --------------- 2025년 4월 20일 일요일 ---------------
    private static final Pattern DATE_LINE_NEW = Pattern.compile(
            "^-{2,}\\s*(\\d{4})년\\s*(\\d{1,2})월\\s*(\\d{1,2})일.*$"
    );

    // 형식 B 대화 라인 예시:
    // [김상원] [오후 4:49] 파일: ...
    // [최서연] [오후 8:08] 이거 보낸 거 어느 부분이 바뀐거에여?
    private static final Pattern TALK_LINE_NEW = Pattern.compile(
            "^\\[(.+?)]\\s*\\[(오전|오후)\\s*(\\d{1,2}):(\\d{2})]\\s*(.*)$"
    );

    /**
     * 공통 파서:
     * 카카오톡 txt 전체를 읽어 TalkData 리스트로 만든다.
     * - 두 형식(옛날/새로운) 모두 지원
     * - 시스템 메시지, 이모티콘/사진/동영상 등은 최대한 필터링
     */
    private static List<TalkData> parseAll(String rawText) {
        List<TalkData> talks = new ArrayList<>();

        if (rawText == null || rawText.isEmpty()) {
            return talks;
        }

        // BOM 제거
        if (rawText.startsWith("\uFEFF")) {
            rawText = rawText.substring(1);
        }

        String[] lines = rawText.split("\\r?\\n");

        boolean started = false;
        LocalDate currentDateForNewFormat = null; // 형식 B 날짜 저장용

        for (String line : lines) {
            if (!started) {
                // "카카오톡 대화" / "OOO 님과 카카오톡 대화" 등 감지
                if (line.contains("카카오톡 대화")) {
                    started = true;
                }
                continue;
            }

            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            // 저장 날짜 줄 스킵
            if (trimmed.startsWith("저장한 날짜")) continue;

            // 형식 B: 날짜 구분선 먼저 체크
            Matcher dateMatcher = DATE_LINE_NEW.matcher(trimmed);
            if (dateMatcher.matches()) {
                int year = Integer.parseInt(dateMatcher.group(1));
                int month = Integer.parseInt(dateMatcher.group(2));
                int day = Integer.parseInt(dateMatcher.group(3));
                currentDateForNewFormat = LocalDate.of(year, month, day);
                continue;
            }

            // 실제 대화 라인 파싱
            String speaker = null;
            String content = null;
            LocalDateTime timestamp = null;

            // 1) 형식 A 시도
            Matcher oldMatcher = TALK_LINE_OLD.matcher(trimmed);
            if (oldMatcher.matches()) {
                int year = Integer.parseInt(oldMatcher.group(1));
                int month = Integer.parseInt(oldMatcher.group(2));
                int day = Integer.parseInt(oldMatcher.group(3));
                String ampm = oldMatcher.group(4);
                int hour12 = Integer.parseInt(oldMatcher.group(5));
                int minute = Integer.parseInt(oldMatcher.group(6));
                speaker = oldMatcher.group(7).trim();
                content = oldMatcher.group(8).trim();

                int hour24 = to24Hour(ampm, hour12);
                timestamp = LocalDateTime.of(year, month, day, hour24, minute);

            } else {
                // 2) 형식 B 시도
                Matcher newMatcher = TALK_LINE_NEW.matcher(trimmed);
                if (newMatcher.matches()) {
                    speaker = newMatcher.group(1).trim();
                    String ampm = newMatcher.group(2);
                    int hour12 = Integer.parseInt(newMatcher.group(3));
                    int minute = Integer.parseInt(newMatcher.group(4));
                    content = newMatcher.group(5).trim();

                    if (currentDateForNewFormat != null) {
                        int hour24 = to24Hour(ampm, hour12);
                        timestamp = currentDateForNewFormat.atTime(hour24, minute);
                    } else {
                        // 혹시 날짜 구분선이 없는 경우, 시간은 null로 둘 수 있음
                        timestamp = null;
                    }
                }
            }

            // 둘 다 아닌 라인은 시스템 메시지/기타 텍스트일 가능성이 크니 스킵
            if (speaker == null || content == null) {
                continue;
            }

            // TalkData 객체 생성
            TalkData talk = new TalkData();
            talk.setSpeaker(speaker);
            talk.setContent(content);
            talk.setTimestamp(timestamp);

            // 실제 대화가 아닌 메시지 필터링
            if (talk.isNonTalk()) continue;     // [OOO님이 나가셨습니다], [사진] 등
            if (talk.isEmoticon()) continue;   // 이모티콘/사진/동영상

            // 추가 필터링: 완전 공백이면 제거
            if (talk.getContent() == null || talk.getContent().trim().isEmpty()) {
                continue;
            }

            talks.add(talk);
        }

        return talks;
    }

    /**
     * MBTI 분석용:
     * 사용자별로 발화 내용만 모아서 Map<String, List<String>> 반환
     */
    public static Map<String, List<String>> parseByUser(String rawText) {
        List<TalkData> talks = parseAll(rawText);
        Map<String, List<String>> result = new HashMap<>();

        for (TalkData talk : talks) {
            String speaker = talk.getSpeaker();
            String msg = talk.getContent();

            if (speaker == null || msg == null) continue;
            if (msg.trim().isEmpty()) continue;

            result.computeIfAbsent(speaker, k -> new ArrayList<>()).add(msg);
        }

        // 디버깅용: 실제 파싱된 참여자 이름 확인
        System.out.println("[KakaoParser] parseByUser 참여자 목록: " + result.keySet());

        return result;
    }

    /**
     * 호감도(crush) 분석용:
     * 전체 대화 중에서 myName, targetName 두 사람의 TalkData만 필터링해서 반환
     * (CrushService.analyze(...)에서 사용)
     */
    public static List<TalkData> parse(String text, String myName, String targetName) {
        List<TalkData> all = parseAll(text);
        List<TalkData> filtered = new ArrayList<>();

        if (myName != null) myName = myName.trim();
        if (targetName != null) targetName = targetName.trim();

        for (TalkData talk : all) {
            String speaker = talk.getSpeaker();
            if (speaker == null) continue;

            if (speaker.equals(myName) || speaker.equals(targetName)) {
                filtered.add(talk);
            }
        }

        System.out.println("[KakaoParser] crush용 필터 결과 개수: " + filtered.size());
        return filtered;
    }

    /**
     * "오전/오후 + 12시간제" -> 24시간제 변환
     */
    private static int to24Hour(String ampm, int hour12) {
        if ("오전".equals(ampm)) {
            if (hour12 == 12) return 0;    // 오전 12시 -> 0시
            return hour12;
        } else { // "오후"
            if (hour12 == 12) return 12;   // 오후 12시 -> 12시
            return hour12 + 12;
        }
    }
}
