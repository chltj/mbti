package com.example.k_mbti.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ChatService {

    private final Random random = new Random();

    // 여러 후보 중 하나 랜덤 선택
    private String pick(String... candidates) {
        if (candidates == null || candidates.length == 0) return "";
        return candidates[random.nextInt(candidates.length)];
    }

    // 상황 감지 (확장)
// 상황 감지 (강화 버전)
// 상황 감지 (강화 버전)
private String detect(String msg) {
    if (msg == null) msg = "";
    msg = msg.toLowerCase().trim();

    if (msg.isBlank()) {
        return "other";
    }

    // -----------------------------
    // 0. 아주 짧은 답변들 (우선 처리)
    // -----------------------------

    // 긍정 (응/어/네/ㅇㅇ/ㅇㅋ/그래/맞아/yes/ok 등)
    if (msg.matches("^(응|웅|어|ㅇㅇ|ㅇㅋ|ㅇㅇㅇ|넹|네|넵|예|옙|그래|그럼|좋아|ok|okay|yes|yep|yup)\\s*$")) {
        return "agree";
    }

    // 부정 (아니/노/ㄴㄴ/싫어 등)
    if (msg.matches("^(아니|노|ㄴㄴ|ㄴㅇㄴ|노노|싫어|아냐|no|nope)\\s*$")) {
        return "disagree";
    }

    // ❗ 없음 / 잘 모름류 (없어, 딱히 없어, 몰라 등)
    if (msg.matches("^(없어|없음|딱히 없어|딱히 없음|글쎄?|모르겠어|잘 모르겠어|몰라)\\s*$")) {
        return "none";
    }

    // ❗ 망설임/생각 중 (음.., 흠.., 글쎄 등)
    if (msg.matches("^(음+\\.*|으음+\\.*|흠+\\.*|글쎄+|well\\.*)\\s*$")) {
        return "thinking";
    }

    // ❗ 웃음 (ㅋㅋ, ㅎㅎ, lol 등)
    if (msg.matches("^(ㅋ+|ㅎ+|ㅋㅋ+|ㅎㅎ+|크크+|lol+|lmao+|rofl+)$")
            || msg.contains("ㅋㅋ") || msg.contains("ㅎㅎ")) {
        return "laugh";
    }

    // ❗ 놀람/충격 (헐, 헉, omg 등)
    if (msg.matches("^(헐+|헉+|허걱+|세상에+|맙소사+|omg+|oh my god+)$")) {
        return "surprise";
    }

    // -----------------------------
    // 1. 문장 안에 포함되는 키워드들
    // -----------------------------

    // 인사
    if (msg.contains("안녕") || msg.contains("하이") || msg.contains("헬로") ||
        msg.contains("hello") || msg.contains("hi") ||
        msg.contains("굿모닝") || msg.contains("굿나잇") || msg.contains("잘 자") || msg.contains("잘자")) {
        return "greeting";
    }

    // 감사
    if (msg.contains("고마워") || msg.contains("고맙") || msg.contains("감사") ||
        msg.contains("땡큐") || msg.contains("thanks") || msg.contains("thank you")) {
        return "thanks";
    }

    // 사과
    if (msg.contains("미안") || msg.contains("죄송") || msg.contains("쏘리") ||
        msg.contains("sorry") || msg.contains("ㅈㅅ")) {
        return "apology";
    }

    // 칭찬/좋아/사랑
    if (msg.contains("좋아해") || msg.contains("사랑해") || msg.contains("너 좋아") ||
        msg.contains("최고") || msg.contains("멋져") || msg.contains("대단해") ||
        msg.contains("존경") || msg.contains("너 짱") || msg.contains("짱이야")) {
        return "compliment";
    }

    // 기쁨/신남/설렘 (happy)
    if (msg.contains("행복") || msg.contains("신나") || msg.contains("신났") ||
        msg.contains("설레") || msg.contains("설렘") ||
        msg.contains("기분 좋") || msg.contains("좋은 하루") ||
        msg.contains("좋았다") || msg.contains("즐거웠") || msg.contains("즐거워")) {
        return "happy";
    }

    // 화남/짜증 (angry)
    if (msg.contains("화나") || msg.contains("화남") || msg.contains("빡치") ||
        msg.contains("빡쳐") || msg.contains("짜증") ||
        msg.contains("열받") || msg.contains("어이없") || msg.contains("개빡")) {
        return "angry";
    }

    // 불안/걱정/긴장 (anxious)
    if (msg.contains("걱정") || msg.contains("불안") || msg.contains("긴장") ||
        msg.contains("초조") || msg.contains("무섭") ||
        msg.contains("두렵") || msg.contains("걱정돼") || msg.contains("불안해")) {
        return "anxious";
    }

    // 날씨
    if (msg.contains("날씨") || msg.contains("덥") || msg.contains("춥") ||
        msg.contains("비와") || msg.contains("비 와") ||
        msg.contains("장마") || msg.contains("눈와") || msg.contains("눈 와") ||
        msg.contains("미세먼지") || msg.contains("황사")) {
        return "weather";
    }

    // 취미/관심사
    if (msg.contains("취미") || msg.contains("좋아하는") || msg.contains("관심") ||
        msg.contains("요즘 뭐에 빠졌") || msg.contains("즐겨 하는") ||
        msg.contains("시간 날 때 뭐") || msg.contains("놀 때 뭐")) {
        return "hobby";
    }

    // 감정/상태 물어보기
    if (msg.contains("기분") || msg.contains("어때") || msg.contains("괜찮아") ||
        msg.contains("요즘 어때") || msg.contains("오늘 어땠어")) {
        return "emotion";
    }

    // 위로 필요 느낌
    if (msg.contains("힘들") || msg.contains("스트레스") || msg.contains("우울") ||
        msg.contains("슬프") || msg.contains("지쳤") || msg.contains("번아웃") ||
        msg.contains("버거워") || msg.contains("포기하고 싶") || msg.contains("현타")) {
        return "comfort";
    }

    // 음식/배고픔
    if (msg.contains("밥") || msg.contains("밥먹") || msg.contains("밥 먹") ||
        msg.contains("먹고 싶") || msg.contains("먹을까") ||
        msg.contains("음식") || msg.contains("배고파") || msg.contains("배고픔") ||
        msg.contains("야식") || msg.contains("메뉴")) {
        return "food";
    }

    // 지금 뭐하는지 (activity)
    if (msg.contains("뭐해") || msg.contains("뭐 해") || msg.contains("하고 있어") ||
        msg.contains("뭐하") || msg.contains("지금 뭐") ||
        msg.contains("뭐 하는 중") || msg.contains("뭐 하는중")) {
        return "activity";
    }

    // 일정/시간/약속 (schedule)
    if (msg.contains("내일") || msg.contains("모레") || msg.contains("언제") ||
        msg.contains("시간 돼") || msg.contains("시간 어때") ||
        msg.contains("약속") || msg.contains("스케줄") || msg.contains("일정")) {
        return "schedule";
    }

    // 피곤/졸림 (tired)
    if (msg.contains("피곤") || msg.contains("졸려") || msg.contains("졸립") ||
        msg.contains("잠와") || msg.contains("잠 와") ||
        msg.contains("잠온") || msg.contains("자고 싶") ||
        msg.contains("녹초") || msg.contains("기절직전")) {
        return "tired";
    }

    // 심심/재미없음 (bored)
    if (msg.contains("심심") || msg.contains("재미없") || msg.contains("재미가 없") ||
        msg.contains("지루") || msg.contains("할 게 없") ||
        msg.contains("할거 없") || msg.contains("할 거 없")) {
        return "bored";
    }

    // 공부/시험/과제/일/회사 (work)
    if (msg.contains("공부") || msg.contains("시험") || msg.contains("과제") ||
        msg.contains("숙제") || msg.contains("레포트") || msg.contains("보고서") ||
        msg.contains("회사") || msg.contains("직장") || msg.contains("야근") ||
        msg.contains("업무") || msg.contains("프로젝트")) {
        return "work";
    }

    // 운동/헬스 (exercise)
    if (msg.contains("운동") || msg.contains("헬스") || msg.contains("런닝") ||
        msg.contains("달리기") || msg.contains("조깅") ||
        msg.contains("요가") || msg.contains("필라테스") ||
        msg.contains("pt 받") || msg.contains("헬스장")) {
        return "exercise";
    }

    // 선택/고민/결정 (decision)
    if (msg.contains("고민") || msg.contains("선택") || msg.contains("결정") ||
        msg.contains("어떡해") || msg.contains("어쩌지") ||
        msg.contains("뭘 해야") || msg.contains("뭐가 나을까") ||
        msg.contains("택해야") || msg.contains("골라야")) {
        return "decision";
    }

    // 궁금/질문 (wondering)
    if (msg.contains("뭐가") || msg.contains("어떤") || msg.contains("무엇") ||
        msg.contains("궁금") || msg.contains("알고 싶") ||
        msg.contains("질문") || msg.endsWith("?")) {
        return "wondering";
    }

    return "other";
}




   public String generateReply(String mbti, String userMsg) {
    if (mbti == null || mbti.isBlank()) mbti = "ENFP";
    mbti = mbti.toUpperCase();

    String ctx = detect(userMsg);

    return switch (ctx) {
        case "agree"      -> agreeReply(mbti);
        case "disagree"   -> disagreeReply(mbti);

        case "greeting"   -> greetingReply(mbti);
        case "thanks"     -> thanksReply(mbti);
        case "apology"    -> apologyReply(mbti);

        case "compliment" -> complimentReply(mbti);
        case "happy"      -> happyReply(mbti);
        case "angry"      -> angryReply(mbti);
        case "anxious"    -> anxiousReply(mbti);

        case "weather"    -> weatherReply(mbti);
        case "hobby"      -> hobbyReply(mbti);

        case "emotion"    -> emotionReply(mbti);
        case "comfort"    -> comfortReply(mbti);

        case "food"       -> foodReply(mbti);
        case "activity"   -> activityReply(mbti);
        case "schedule"   -> scheduleReply(mbti);

        case "tired"      -> tiredReply(mbti);
        case "bored"      -> boredReply(mbti);

        case "work"       -> workReply(mbti);
        case "exercise"   -> exerciseReply(mbti);

        case "decision"   -> decisionReply(mbti);
        case "wondering"  -> wonderingReply(mbti);

        // 🔹 새로 추가된 상황들
        case "none"       -> noneReply(mbti);
        case "thinking"   -> thinkingReply(mbti);
        case "laugh"      -> laughReply(mbti);
        case "surprise"   -> surpriseReply(mbti);

        default           -> genericReply(mbti);
    };
}


    // ----------------- 새로 추가된/강화된 답변 메소드들 -----------------

    private String agreeReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "헉 그렇구나!! 😆 더 얘기해줘!",
                    "오호 맞지맞지~ 나도 그렇게 생각했어!",
                    "완전 공감이야ㅋㅋ 그런 생각한 거 진짜 멋있다!"
            );
            case "ENFJ" -> pick(
                    "응응~ 좋아! 그럼 이제 뭐할까?",
                    "맞아, 그런 방향이 더 좋을 것 같아.",
                    "나도 그렇게 느꼈어. 계속 얘기해줘~"
            );
            case "ENTP" -> pick(
                    "오케이ㅋㅋ 그럼 다음은?",
                    "좋아, 그 생각 재밌는데? 더 파볼까?",
                    "응, 그 말에 논리 꽤 있네. 계속 말해봐~"
            );
            // 나머지 MBTI들도 같은 패턴으로 2~3개씩 추가
            case "INTP" -> pick(
                    "음… 알겠어. 계속해봐.",
                    "논리적으로 들리네. 좀 더 자세히 말해줄래?",
                    "그 가설 흥미로운데, 예시도 있어?"
            );
            default -> pick(
                    "응응! 알겠어!",
                    "맞아, 나도 그렇게 생각해~",
                    "좋다! 계속 얘기해줘."
            );
        };
    }

    private String disagreeReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "앗 아니야?? 그럼 뭐야? 말해봐! 😮",
                    "헐 내가 잘못 이해했나봐ㅠㅠ 어떻게 된 건데?",
                    "엇? 다른 생각 있어?? 완전 궁금해!"
            );
            case "INTJ" -> pick(
                    "아니면 정정해줘. 정확히 뭐야?",
                    "흥미롭네. 그럼 네 기준에서 정답은 뭐야?",
                    "좋아, 다른 관점 들을 준비됐어. 말해봐."
            );
            default -> pick(
                    "아 그렇구나! 그럼 뭐야?",
                    "내가 조금 다르게 이해했나 보다. 설명해줄래?",
                    "음, 네 생각 궁금하다. 말해줘."
            );
        };
    }

    private String greetingReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "안녕!! 😆 오늘 뭐 재밌는 일 있어??",
                    "오 안녕안녕~ 나와 수다 떨 준비 됐어? ㅎㅎ",
                    "헬로우우!! 오늘 기분은 어때??"
            );
            case "INTP" -> pick(
                    "안녕. 요즘 뭐 재밌는 거 생각하고 있어?",
                    "안녕~ 또 이상한(?) 생각 많이 했어? 말해봐 ㅎㅎ",
                    "어, 안녕. 요즘 호기심 생긴 주제 있어?"
            );
            default -> pick(
                    "안녕! 반가워~",
                    "오랜만이네! 잘 지냈어?",
                    "안녕안녕~ 오늘 하루 어땠어?"
            );
        };
    }

    private String thanksReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "헉 뭘!! 우리 사이에 그런 거 없잖아 😆",
                    "에이 고마워할 일도 아닌데 ㅎㅎ",
                    "나도 즐거웠어! 또 말 걸어줘~"
            );
            default -> pick(
                    "천만에~ 언제든지 말해줘!",
                    "도움됐다니 다행이야.",
                    "별거 아니야! 또 필요하면 말해."
            );
        };
    }

    private String apologyReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "에이 그런 말 하지 마~ 괜찮아 진짜 😆",
                    "뭐야 귀엽게 왜 사과해 ㅋㅋ 완전 괜찮은데?",
                    "나는 하나도 안 서운했어! 걱정 노노~"
            );
            case "INFJ" -> pick(
                    "사과해줘서 고마워. 그 마음이 더 소중해.",
                    "괜찮아. 너도 많이 신경 썼던 것 같아.",
                    "그 상황에서 그럴 수도 있어. 너무 자책하지 마."
            );
            default -> pick(
                    "괜찮아, 신경 쓰지 마~",
                    "알려줘서 고마워. 난 괜찮아!",
                    "누구나 그럴 수 있어. 다음에 더 좋게 해보자~"
            );
        };
    }
    // 1) "없어", "몰라" 같은 대답 (none)
private String noneReply(String m) {
    return switch (m) {
        case "ENFP" -> pick(
                "없어…? 그럼 내가 하나 추천해줄까? 😆",
                "딱히 없는 것도 괜찮지 ㅎㅎ 그럼 요즘엔 뭐에 관심 있어?",
                "없을 수도 있지~ 대신 나랑 아무 말 대잔치 할래? ㅋㅋ"
        );
        case "INFJ" -> pick(
                "지금은 떠오르지 않을 수도 있어. 천천히 생각해봐도 돼.",
                "없다는 것도 하나의 답이야. 괜찮아.",
                "굳이 억지로 만들 필요는 없어. 나중에 생기면 말해줘."
        );
        default -> pick(
                "없구나~ 괜찮아, 굳이 있어야 하는 건 아니니까 ㅎㅎ",
                "없을 수도 있지 뭐! 그럼 요즘엔 뭐가 제일 편해?",
                "없다고 말해주는 것도 솔직해서 좋다 😊"
        );
    };
}

// 2) "음..", "흠.." 같은 망설임/생각 중 (thinking)
private String thinkingReply(String m) {
    return switch (m) {
        case "ENFP" -> pick(
                "오… 고민하는 그 표정이 보이는 것 같아 ㅋㅋ 뭐 떠오르는 거 있어?",
                "음~~~ 그러니까 아직 정리가 안 된 거지? 편하게 말해도 돼!",
                "천천히 생각해도 괜찮아! 떠오르면 아무 때나 말해줘 😆"
        );
        case "INTP" -> pick(
                "지금 머릿속에서 정리 중이지? ㅋㅋ 다 끝난 다음에 말해도 돼.",
                "논리 구성 중인 거구만… 기다릴게 ㅎㅎ",
                "애매하면 애매하다고 말해도 돼. 그 상태도 정보니까."
        );
        default -> pick(
                "천천히 생각해도 괜찮아~ 서두를 필요 없어.",
                "정 안 떠오르면 그냥 느낌대로 말해도 돼 ㅎㅎ",
                "애매하면 애매하다고 말해줘도 괜찮아. 그게 자연스러워."
        );
    };
}

// 3) "ㅋㅋ", "ㅎㅎ" 같은 웃음 (laugh)
private String laughReply(String m) {
    return switch (m) {
        case "ENFP" -> pick(
                "ㅋㅋㅋㅋ 나도 웃겨 지금 ㅋㅋ",
                "왜 이렇게 웃겨 ㅋㅋ 뭐 상상했어?",
                "같이 웃으니까 괜히 기분 좋다 😆"
        );
        case "ENTP" -> pick(
                "왜 웃냐 ㅋㅋ 나도 궁금하잖아.",
                "ㅋㅋ 이 반응 뭔가 수상한데? 디테일 풀어라.",
                "웃음 코드 맞는 거 같은데 더 풀어봐 ㅋㅋ"
        );
        default -> pick(
                "ㅋㅋㅋ 웃음 터졌네 ㅎㅎ",
                "나도 괜히 따라 웃게 된다 ㅋㅋ",
                "웃을 일 있는 건 좋은 거다 😊 계속 얘기해줘~"
        );
    };
}

// 4) "헐", "헉", "omg" 같은 놀람 (surprise)
private String surpriseReply(String m) {
    return switch (m) {
        case "ENFP" -> pick(
                "헐 뭐야? 무슨 일 있었어?? 😳",
                "헉 진짜?? 디테일 빨리 말해줘!!",
                "세상에… 상상도 못한 전개인데? ㅋㅋ"
        );
        case "INTJ" -> pick(
                "상당히 예상 밖이었나 보네. 어떤 상황이었어?",
                "그 정도면 변수로 처리 못 한 사건이네. 자세히 말해줘.",
                "흥미로운 상황이네. 분석 거리 생겼다."
        );
        default -> pick(
                "헐, 진짜 그런 일이 있었어?",
                "헉… 듣기만 해도 놀랍다. 어떻게 된 거야?",
                "상상도 못 했겠다… 천천히 이야기해줘."
        );
    };
}


    private String happyReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "우와 행복하다니 나까지 기분 좋아져 😆 무슨 일 있었어??",
                    "헉 설렌다 설레!! 자세히 말해줘 제발 >_<",
                    "좋은 일 있었구나!! 그 얘기 끝까지 들어야겠다 ㅎㅎ"
            );
            case "ISFP" -> pick(
                    "행복한 순간 잘 느끼고 있구나… 그런 시간 진짜 소중해.",
                    "와… 듣기만 해도 따뜻하다. 더 말해줄래?",
                    "그 감정 오래 기억해두면 나중에 힘날 거야."
            );
            default -> pick(
                    "와, 기분 좋다는 말 들으니까 나도 좋다!",
                    "좋은 일 있었구나! 어떤 일이었어?",
                    "그 기분 오래오래 갔으면 좋겠다 😊"
            );
        };
    }

    private String angryReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "헉 진짜 열받는 일이네?? 😡 누가 그랬어 말해봐!",
                    "와 그건 나라도 화났겠다… 디테일 좀 풀어줘.",
                    "으악 빡치네 진짜;; 나랑 같이 욕이라도 해줄까 ㅋㅋ"
            );
            case "INTJ" -> pick(
                    "상당히 불합리한 상황이었나 보네. 원인이 뭐였어?",
                    "감정도 중요하지만, 재발 방지가 더 중요하지. 어떻게 막을 수 있을까?",
                    "그 상황에서 화나는 게 당연해. 다만 전략적으로 풀 방법도 같이 보자."
            );
            default -> pick(
                    "진짜 스트레스였겠다… 무슨 일이었는지 말해줄래?",
                    "그 정도면 화날 만하다. 네 편이야.",
                    "일단 마음부터 풀고, 그 다음에 해결 방법 같이 생각해보자."
            );
        };
    }

    private String anxiousReply(String m) {
        return switch (m) {
            case "INFP" -> pick(
                    "많이 걱정되고 불안했겠다… 천천히 어떤 상황인지 말해줄래?",
                    "그 마음 너무 이해돼… 혼자 끌어안지 말고 나한테 좀 나눠줘.",
                    "괜찮아, 완벽하지 않아도 돼. 네 속마음 그대로 말해줘도 돼."
            );
            case "INFJ" -> pick(
                    "불안한 감정도 다 이유가 있어. 그 이유를 같이 찾아보자.",
                    "괜찮아, 지금처럼 솔직하게 말해주는 것만으로도 큰 걸음이야.",
                    "네가 느끼는 걱정을 가볍게 보지 않을게. 하나씩 차분히 들어볼게."
            );
            default -> pick(
                    "요즘 좀 불안했구나… 어떤 부분이 제일 걱정돼?",
                    "혼자 고민하지 말고 같이 나눠보자.",
                    "불안한 마음이 계속되면 더 힘들어지니까, 지금 말해줘서 잘한 거야."
            );
        };
    }

    // ----------------- 기존 감정/상황 메소드들도 랜덤화 -----------------

    private String emotionReply(String m) {
        return switch (m) {
            case "INFP" -> pick(
                    "음… 너 요즘 마음이 조금 예민해진 것 같아. 이야기해줄래? 내가 들어줄게.",
                    "요즘 감정이 롤러코스터 같지 않았어…? 괜찮으면 하나씩 말해줘.",
                    "네 기분이 어떤지 궁금해. 솔직하게 말해줘도 돼."
            );
            case "ENFP" -> pick(
                    "헉 😮 무슨 일 있었던 거야?? 나 완전 집중 모드 ON이야!",
                    "요즘 기분이 좀 복잡했어?? 나한테 한 번 털어놔봐!",
                    "좋아도, 힘들어도 그냥 다 말해도 돼. 나 듣는 거 좋아해 ㅎㅎ"
            );
            default -> pick(
                    "기분이 안 좋았던 거야? 괜찮아, 말해줘.",
                    "요즘 마음 상태가 어떤지 궁금해. 편하게 얘기해줘.",
                    "감정에는 다 이유가 있어. 그 얘기 같이 풀어보자."
            );
        };
    }

    private String comfortReply(String m) {
        return switch (m) {
            case "INFP" -> pick(
                    "아… 그거 진짜 마음 아팠겠다… 너 그러려고 그런 게 아니야.",
                    "그 상황이면 누구라도 힘들었을 거야. 너만 그런 거 아니야.",
                    "네가 얼마나 버티느라 애썼는지 느껴져… 조금만 나랑 같이 쉬자."
            );
        // 나머지도 필요하면 비슷하게 확장 가능
            default -> pick(
                    "고생 많았어… 조금 쉬어도 괜찮아.",
                    "정말 힘든 시간 보냈겠다. 지금 여기까지 온 것만도 대단해.",
                    "지금만큼은 네 편 하나쯤 있어도 되잖아. 나 여기 있어."
            );
        };
    }

    private String foodReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "밥!!! 😆 뭐 먹고 싶어? 맛있는 얘기만 해도 기분 좋아져~",
                    "헉 야식 타임인가요? ㅋㅋ 뭐 땡겨 지금?",
                    "메뉴 고민 중이야?? 내가 진지하게 같이 고민해줄게… 🍜🍕"
            );
            default -> pick(
                    "뭐 먹고 싶어?",
                    "따뜻한 거 먹으면 기분 좋아질 텐데… 어떤 게 끌려?",
                    "배고프면 아무것도 안 되지 ㅋㅋ 메뉴 골라보자."
            );
        };
    }

    private String activityReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "나?? 너 생각 ㅋㅋ 장난이고 뭐해?? 😆",
                    "그냥 이것저것 하다가 네 톡 보고 냅다 달려왔지 ㅋㅋ",
                    "멍 때리다가 네가 생각나서 폰 켰어 ㅎㅎ 너는 뭐해?"
            );
            default -> pick(
                    "응, 너는 뭐하고 있어?",
                    "나도 그냥 이것저것 하다가 쉬는 중이었어. 너는?",
                    "요즘은 뭐하면서 시간 보내고 있어?"
            );
        };
    }

    private String scheduleReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "오 내일?? 뭐 좋은 일 생기나?? 😆",
                    "내일 얘기 나오는 거 보니까 뭔가 준비 중이지? ㅋㅋ",
                    "혹시 내일 중요한 날이야?? 자세히 말해줘~"
            );
            default -> pick(
                    "내일 일정 말하는 거지?",
                    "내일 뭐 계획 있어? 궁금하다.",
                    "시간대 정해지면 알려줘. 그때 맞춰서 얘기해보자."
            );
        };
    }

    private String tiredReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "헉 피곤해?? 😢 잠깐 쉬어!! 무리하지 마!",
                    "완전 녹초구나… ㅠㅠ 잠깐 아무것도 하지 말고 쉬자.",
                    "오늘도 진짜 수고했어. 일단 물 한 잔 마시고 숨 좀 고르자."
            );
            default -> pick(
                    "많이 피곤했나 봐… 쉬어!",
                    "피곤할 땐 잠깐이라도 눈 감고 쉬는 게 최고야.",
                    "몸이 신호 보내는 거야. 오늘은 좀 일찍 자는 게 어때?"
            );
        };
    }

    private String boredReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "심심해?? 나랑 놀자!! 😆 뭐하고 싶어?",
                    "지루한 시간은 수다로 채워야지 ㅋㅋ 뭐 얘기할까?",
                    "심심하면 괜히 이상한 생각 많이 나지 않냐 ㅋㅋ 나랑 시간 죽여보자!"
            );
            default -> pick(
                    "심심해? 뭐하고 싶어?",
                    "그럼 우리 잡담이나 할까? 요즘 관심 있는 거 있어?",
                    "심심할 때는 새로운 취미 찾기 딱 좋은 타이밍인데 ㅎㅎ"
            );
        };
    }

    private String workReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "공부/일?? 화이팅!! 😆 힘들면 쉬엄쉬엄 해!",
                    "와 열심히 한다… 근데 가끔은 쉬는 것도 스킬이야!",
                    "하기 싫어도 하는 거 진짜 대단한 거야. 나도 응원할게!"
            );
            default -> pick(
                    "열심히 하는구나! 힘내!",
                    "고생 많다… 그래도 하나씩 하고 있으니까 분명 쌓일 거야.",
                    "힘들면 잠깐 스트레칭이라도 하고 와서 다시 하자."
            );
        };
    }

    private String exerciseReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "운동?? 나도 같이 할래!! 😆 뭐해?",
                    "우와 운동하는 사람 멋있다 ㅋㅋ 어떤 운동하는데?",
                    "운동하면 기분도 좋아지잖아! 끝나고 뭐 먹을지도 중요하지 ㅋㅋ"
            );
            default -> pick(
                    "운동 좋지! 뭐해?",
                    "꾸준히 하면 진짜 달라질 거야. 오늘도 화이팅!",
                    "무리만 안 하게 조심하고, 끝나고 꼭 스트레칭 해줘~"
            );
        };
    }

    private String decisionReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "고민돼?? 같이 생각해보자!! 뭐 때문에 그래? 😮",
                    "우선 네 마음이 어디로 더 가는지부터 말해줘!",
                    "고민 얘기하는 순간부터 이미 반은 정리된 거야 ㅎㅎ 말해봐!"
            );
            default -> pick(
                    "고민이구나… 같이 생각해보자!",
                    "각 선택지의 장단점부터 정리해보자. 어떤 선택이 있어?",
                    "어느 쪽이든 네가 후회 없을 선택이었으면 좋겠다. 옵션이 뭐야?"
            );
        };
    }

    private String wonderingReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "뭐가 궁금해?? 😆 궁금한 거 다 말해줘!",
                    "궁금한 거 생겼다는 건 이미 시작이 재밌다는 거지 ㅋㅋ 말해봐!",
                    "어떤 게 궁금해? 나 설명충 모드 ON 한다 🤓"
            );
            default -> pick(
                    "뭐가 궁금해~? 말해줘!",
                    "궁금한 거 있으면 하나씩 물어봐도 돼.",
                    "생각나는 대로 물어봐줘. 같이 파보자!"
            );
        };
    }

    private String genericReply(String m) {
        return switch (m) {
            case "ENFP" -> pick(
                    "와 재밌다 ㅋㅋㅋ 더 얘기해봐 😄",
                    "너 얘기 듣다 보니까 시간 순삭이네 ㅋㅋ 계속 말해줘!",
                    "오 이거 흥미진진한데? 이어서 말해봐!"
            );
            default -> pick(
                    "응응~ 계속 얘기해줘!",
                    "흥미롭네. 더 듣고 싶다.",
                    "좋은 얘기인데? 이어서 말해줄래?"
            );
        };
    }
    private String complimentReply(String m) {
    return switch (m) {
        case "ENFP" -> pick(
                "헉 진짜?? 😳 너도 진짜 최고야!!",
                "와 그렇게 말해줘서 기분 너무 좋다 ㅋㅋ",
                "에이 나보다 네가 더 멋있지 뭐 😆"
        );
        case "ENFJ" -> pick(
                "고마워~ 너도 정말 멋진 사람이야!",
                "그렇게 말해줘서 힘난다 진짜.",
                "너가 해주는 말은 항상 따뜻해 😊"
        );
        case "INTP" -> pick(
                "음… 고마워. 그런 말 들으니까 묘하게 좋네.",
                "의외의 칭찬이네 ㅋㅋ 그래도 기분은 좋다.",
                "데이터 상으로도 나쁘지 않은 평가 같군…(?)"
        );
        // 필요하면 다른 MBTI도 추가 가능
        default -> pick(
                "고마워! 너도 정말 멋져!",
                "그 말 듣고 하루 기분 좋아졌다 😊",
                "칭찬해줘서 고마워. 나도 너 좋게 보고 있어!"
        );
    };
}
private String weatherReply(String m) {
    return switch (m) {
        case "ENFP" -> pick(
                "날씨?? 오늘 날씨 진짜 어때? 나가서 놀기 좋아? 😆",
                "날씨 좋으면 그냥 나가야지 ㅋㅋ 어디 가고 싶어?",
                "비 오면 감성 터지고, 맑으면 텐션 터지지 않냐 ㅋㅋ"
        );
        case "INFJ" -> pick(
                "날씨 따라 기분도 왔다 갔다 하지… 오늘은 어때 보여?",
                "하늘이 어떤지에 따라 하루 느낌도 바뀌는 것 같아.",
                "날씨 얘기하는 거 보니까, 네 마음 상태도 살짝 궁금해진다."
        );
        default -> pick(
                "오늘 날씨 어때? 나가기 좋아?",
                "밖에 날씨 괜찮으면 잠깐 산책이라도 하는 거 어때?",
                "날씨가 어떤지에 따라 할 수 있는 것도 달라지지 ㅎㅎ"
        );
    };
}
private String hobbyReply(String m) {
    return switch (m) {
        case "ENFP" -> pick(
                "취미?? 나 취미 엄청 많아!! 😆 너는 뭐 좋아해?",
                "요즘 꽂힌 거 있어?? 나 이런저런 거 다 좋아해 ㅋㅋ",
                "취미 얘기하면 끝도 없는데… 너부터 말해봐!"
        );
        case "INTJ" -> pick(
                "취미? 나름 생산적인 걸 좋아해. 너는?",
                "시간 투자 대비 효율 좋은 취미를 선호하는 편이야.",
                "머리 쓰이는 취미가 좋더라. 너는 어떤 타입이야?"
        );
        default -> pick(
            "나도 취미 많아! 너는 뭐 좋아해?",
            "취미 얘기하는 거 좋다 ㅎㅎ 요즘 뭐에 빠져 있어?",
            "집에서 하는 거랑 밖에서 하는 거, 둘 중에 뭐가 더 좋아?"
        );
    };
}



}
