package com.example.k_mbti.service;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

    // 상황 감지 (확장)
    private String detect(String msg) {
        msg = msg.toLowerCase();

        // 긍정 답변 (응, 네, 그래 등)
        if (msg.matches("^(응|어|네|넵|ㅇㅇ|ㅇㅋ|ok|okay|그래|맞아|yes)$") || 
            msg.matches("^(응|어|네|넵|ㅇㅇ|ㅇㅋ|그래|맞아)\\s*$"))
            return "agree";
        
        // 부정 답변 (아니, 싫어 등)
        if (msg.matches("^(아니|노|ㄴㄴ|no|nope|싫어|아냐)$") || 
            msg.matches("^(아니|노|ㄴㄴ|싫어|아냐)\\s*$"))
            return "disagree";

        // 인사
        if (msg.contains("안녕") || msg.contains("하이") || msg.contains("헬로"))
            return "greeting";
        
        // 감사
        if (msg.contains("고마") || msg.contains("감사") || msg.contains("땡큐"))
            return "thanks";
        
        // 칭찬/사랑
        if (msg.contains("좋아") || msg.contains("사랑") || msg.contains("최고") || msg.contains("멋져"))
            return "compliment";
        
        // 날씨
        if (msg.contains("날씨") || msg.contains("덥") || msg.contains("춥") || msg.contains("비"))
            return "weather";
        
        // 취미/관심사
        if (msg.contains("취미") || msg.contains("좋아하는") || msg.contains("관심"))
            return "hobby";
        
        // 감정
        if (msg.contains("기분") || msg.contains("어때"))
            return "emotion";
        
        // 위로
        if (msg.contains("힘들") || msg.contains("스트레스") || msg.contains("우울") || msg.contains("슬프"))
            return "comfort";
        
        // 음식
        if (msg.contains("밥") || msg.contains("먹") || msg.contains("음식") || msg.contains("배고"))
            return "food";
        
        // 활동
        if (msg.contains("뭐해") || msg.contains("하고 있어") || msg.contains("뭐하"))
            return "activity";
        
        // 일정
        if (msg.contains("내일") || msg.contains("언제") || msg.contains("시간") || msg.contains("약속"))
            return "schedule";
        
        // 피곤/졸림
        if (msg.contains("피곤") || msg.contains("졸려") || msg.contains("잠") || msg.contains("자고"))
            return "tired";
        
        // 심심함
        if (msg.contains("심심") || msg.contains("재미없") || msg.contains("지루"))
            return "bored";
        
        // 공부/일
        if (msg.contains("공부") || msg.contains("시험") || msg.contains("과제") || msg.contains("일") || msg.contains("회사"))
            return "work";
        
        // 운동
        if (msg.contains("운동") || msg.contains("헬스") || msg.contains("달리기") || msg.contains("요가"))
            return "exercise";
        
        // 선택/고민
        if (msg.contains("고민") || msg.contains("선택") || msg.contains("결정") || msg.contains("어떡해"))
            return "decision";

        return "other";
    }

    public String generateReply(String mbti, String userMsg) {
        if (mbti == null || mbti.isBlank()) mbti = "ENFP";
        mbti = mbti.toUpperCase();

        String ctx = detect(userMsg == null ? "" : userMsg);

        return switch (ctx) {
            case "agree"      -> agreeReply(mbti);
            case "disagree"   -> disagreeReply(mbti);
            case "greeting"   -> greetingReply(mbti);
            case "thanks"     -> thanksReply(mbti);
            case "compliment" -> complimentReply(mbti);
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
            default           -> genericReply(mbti);
        };
    }

    // ----------------- 새로 추가된 답변 메소드들 -----------------
    
    private String agreeReply(String m) {
        return switch (m) {
            case "ENFP" -> "헉 그렇구나!! 😆 더 얘기해줘!";
            case "ENFJ" -> "응응~ 좋아! 그럼 이제 뭐할까?";
            case "ENTP" -> "오케이ㅋㅋ 그럼 다음은?";
            case "ENTJ" -> "좋아. 그럼 진행하자.";
            case "ESFP" -> "오예!! 좋아좋아!! 😄";
            case "ESFJ" -> "응응~ 그렇구나! 다행이다!";
            case "ESTP" -> "오케이! 그럼 바로 하자!";
            case "ESTJ" -> "알겠어. 확인했어.";
            case "INFP" -> "응… 그렇구나. 알겠어!";
            case "INFJ" -> "그렇구나. 이해했어.";
            case "INTP" -> "음… 알겠어. 계속해봐.";
            case "INTJ" -> "확인. 다음 단계는?";
            case "ISFP" -> "응… 알겠어.";
            case "ISFJ" -> "그렇구나! 알겠어~";
            case "ISTP" -> "오케이.";
            case "ISTJ" -> "알겠어. 확인했어.";
            default -> "응응! 알겠어!";
        };
    }

    private String disagreeReply(String m) {
        return switch (m) {
            case "ENFP" -> "앗 아니야?? 그럼 뭐야? 말해봐! 😮";
            case "ENFJ" -> "아 그렇구나~ 그럼 어떤 게 맞아?";
            case "ENTP" -> "오 아니야? 그럼 정답은 뭔데?";
            case "ENTJ" -> "아니면 정확히 뭐야? 말해봐.";
            case "ESFP" -> "앗 아니야?? 그럼 뭐가 맞아??";
            case "ESFJ" -> "아 그렇구나! 내가 잘못 알았네!";
            case "ESTP" -> "아니야? 그럼 뭔데?";
            case "ESTJ" -> "아니면 정확히 설명해줘.";
            case "INFP" -> "아… 아니었구나. 그럼 뭐야?";
            case "INFJ" -> "그렇구나. 그럼 무슨 뜻이었어?";
            case "INTP" -> "음… 아니야? 그럼 뭐가 맞지?";
            case "INTJ" -> "아니면 정정해줘. 정확히 뭐야?";
            case "ISFP" -> "아… 아니구나. 말해줘.";
            case "ISFJ" -> "아 그렇구나! 내가 잘못 생각했네!";
            case "ISTP" -> "아니야? 뭔데.";
            case "ISTJ" -> "아니면 정확한 정보 알려줘.";
            default -> "아 그렇구나! 그럼 뭐야?";
        };
    }
    
    private String greetingReply(String m) {
        return switch (m) {
            case "ENFP" -> "안녕!! 😆 오늘 뭐 재밌는 일 있어??";
            case "ENFJ" -> "안녕~ 오늘 하루 어때? 잘 지내고 있어?";
            case "ENTP" -> "안녕ㅋㅋ 오늘 무슨 재밌는 얘기 들려줄 거야?";
            case "ENTJ" -> "어, 안녕. 오늘 계획 잘 진행되고 있어?";
            case "ESFP" -> "헬로우~!! 반가워!! 뭐하고 있었어? 😄";
            case "ESFJ" -> "안녕! 오늘 괜찮아? 뭐 필요한 거 있어?";
            case "ESTP" -> "야! 뭐해? 재밌는 거 하고 있었어?";
            case "ESTJ" -> "안녕. 오늘 할 일 잘 되고 있어?";
            case "INFP" -> "안녕… 오늘은 어떤 하루야? 기분 괜찮아?";
            case "INFJ" -> "안녕, 오늘 기분은 어때? 뭔가 특별한 일 있었어?";
            case "INTP" -> "안녕. 요즘 뭐 재밌는 거 생각하고 있어?";
            case "INTJ" -> "안녕. 오늘 뭔가 성과 있었어?";
            case "ISFP" -> "안녕… 오늘 평화로운 하루 보내고 있어?";
            case "ISFJ" -> "안녕~ 오늘 하루 별일 없었어? 괜찮아?";
            case "ISTP" -> "어. 안녕.";
            case "ISTJ" -> "안녕. 오늘 일정 괜찮아?";
            default -> "안녕! 반가워~";
        };
    }

    private String thanksReply(String m) {
        return switch (m) {
            case "ENFP" -> "헉 뭘!! 우리 사이에 그런 거 없잖아 😆";
            case "ENFJ" -> "천만에~ 언제든지 말해줘!";
            case "ENTP" -> "ㅋㅋㅋ 당연하지! 또 필요하면 말해~";
            case "ENTJ" -> "별거 아냐. 필요하면 또 말해.";
            case "ESFP" -> "에이 뭘~ 당연한 거지!! 😄";
            case "ESFJ" -> "아니야~ 너한테 도움 되면 나도 좋아!";
            case "ESTP" -> "오케이~ 별거 아닌데?";
            case "ESTJ" -> "응, 필요한 일이었으니까.";
            case "INFP" -> "아니야… 네가 행복하면 나도 좋아.";
            case "INFJ" -> "괜찮아, 너한테 도움이 됐다니 다행이야.";
            case "INTP" -> "음… 별거 아니었는데. 괜찮아.";
            case "INTJ" -> "당연한 거지. 문제 해결이 중요하니까.";
            case "ISFP" -> "아니야… 도움이 됐다면 기뻐.";
            case "ISFJ" -> "아니야~ 너 도와줄 수 있어서 기뻐!";
            case "ISTP" -> "음. 별거 아냐.";
            case "ISTJ" -> "응, 당연한 일이었어.";
            default -> "천만에! 언제든 말해줘~";
        };
    }

    private String complimentReply(String m) {
        return switch (m) {
            case "ENFP" -> "헉 진짜?? 😳 너도 진짜 최고야!!";
            case "ENFJ" -> "고마워~ 너도 정말 멋진 사람이야!";
            case "ENTP" -> "ㅋㅋㅋ 알아줘서 고맙네? 너도 좋은데?";
            case "ENTJ" -> "당연하지. 너도 훌륭해.";
            case "ESFP" -> "꺄 고마워!! 너도 완전 좋아!! 😆";
            case "ESFJ" -> "진짜? 너 말 들으니까 기분 너무 좋다!";
            case "ESTP" -> "오 고맙네! 너도 괜찮은데?";
            case "ESTJ" -> "고마워. 너도 인정할 만해.";
            case "INFP" -> "와… 그렇게 생각해줘서 고마워… 진심이야?";
            case "INFJ" -> "고마워… 네 말이 정말 따뜻하게 느껴져.";
            case "INTP" -> "음… 고마워. 그렇게 말해주니 좋네.";
            case "INTJ" -> "고맙네. 객관적인 평가 같아서 믿을 만해.";
            case "ISFP" -> "정말…? 고마워… 기분 좋다.";
            case "ISFJ" -> "헉 고마워… 너무 좋은 말이야!";
            case "ISTP" -> "음. 고맙네.";
            case "ISTJ" -> "고마워. 인정받는 건 좋은 일이야.";
            default -> "고마워! 너도 정말 멋져!";
        };
    }

    private String weatherReply(String m) {
        return switch (m) {
            case "ENFP" -> "날씨?? 오늘 날씨 진짜 어때? 나가서 놀기 좋아? 😆";
            case "ENFJ" -> "날씨가 안 좋으면 몸조리 잘해야 해. 괜찮아?";
            case "ENTP" -> "날씨 얘기? 갑자기 산책이라도 가고 싶은 거야?";
            case "ENTJ" -> "날씨에 맞춰서 계획 조정해야겠네.";
            case "ESFP" -> "날씨 좋으면 나가서 놀자!! 안 좋으면 집콕! 😄";
            case "ESFJ" -> "날씨 안 좋으면 감기 조심해야 해!";
            case "ESTP" -> "날씨? 뭐 할 만한 거 있으면 바로 나가자!";
            case "ESTJ" -> "날씨 체크는 중요하지. 외출 계획 있어?";
            case "INFP" -> "날씨가 기분에 영향 주는 것 같아… 너는 어때?";
            case "INFJ" -> "날씨에 따라 기분이 달라지기도 하지. 오늘은 어때?";
            case "INTP" -> "날씨… 음, 기압 변화가 영향 주나?";
            case "INTJ" -> "날씨 데이터 확인해서 계획 세워야겠어.";
            case "ISFP" -> "날씨 좋으면 기분도 좋아지는 것 같아…";
            case "ISFJ" -> "날씨 안 좋으면 따뜻하게 있어야 해!";
            case "ISTP" -> "날씨? 뭐 할 거 있으면 상관없지 않나.";
            case "ISTJ" -> "날씨 확인은 기본이지. 준비 잘했어?";
            default -> "날씨 어때? 나가기 좋아?";
        };
    }

    private String hobbyReply(String m) {
        return switch (m) {
            case "ENFP" -> "취미?? 나 취미 엄청 많아!! 😆 너는 뭐 좋아해?";
            case "ENFJ" -> "취미? 난 사람들이랑 있는 게 제일 좋아! 너는?";
            case "ENTP" -> "취미 많지~ 새로운 거 배우는 것도 좋아. 너는?";
            case "ENTJ" -> "취미? 생산적인 걸 좋아해. 너는 뭐 해?";
            case "ESFP" -> "나?? 노는 거!! ㅋㅋㅋ 너는 뭐 좋아해?";
            case "ESFJ" -> "친구들이랑 있는 거! 너는 어떤 거 좋아해?";
            case "ESTP" -> "움직이는 거 다 좋아! 너는?";
            case "ESTJ" -> "취미로 운동 좀 해. 너는?";
            case "INFP" -> "음악 듣거나 글 쓰는 거… 너는 뭐 좋아해?";
            case "INFJ" -> "책 읽는 거 좋아해. 너는?";
            case "INTP" -> "뭔가 배우는 거? 너는 뭐 하는데?";
            case "INTJ" -> "지식 쌓는 거. 너는 어떤 취미 있어?";
            case "ISFP" -> "예술적인 거… 그림 그리거나. 너는?";
            case "ISFJ" -> "집에서 편하게 쉬는 거! 너는?";
            case "ISTP" -> "뭐든 만드는 거. 너는?";
            case "ISTJ" -> "정리하는 거? 너는 뭐 해?";
            default -> "나도 취미 많아! 너는 뭐 좋아해?";
        };
    }

    private String tiredReply(String m) {
        return switch (m) {
            case "ENFP" -> "헉 피곤해?? 😢 잠깐 쉬어!! 무리하지 마!";
            case "ENFJ" -> "많이 피곤했나 보네… 좀 쉬어. 괜찮아?";
            case "ENTP" -> "피곤하면 뇌가 안 돌아가지. 잠깐 쉬는 게 나아.";
            case "ENTJ" -> "피곤하면 효율 떨어져. 휴식 필요해.";
            case "ESFP" -> "피곤해?? ㅠㅠ 좀 자!! 푹 쉬어야 해!";
            case "ESFJ" -> "많이 피곤하구나… 무리하지 말고 쉬어!";
            case "ESTP" -> "피곤하면 쉬어야지. 잠깐 눈 붙여봐.";
            case "ESTJ" -> "피곤하면 생산성 떨어져. 제대로 쉬어.";
            case "INFP" -> "많이 힘들었나 봐… 따뜻하게 쉬어…";
            case "INFJ" -> "무리했구나… 좀 쉬면서 회복해야 해.";
            case "INTP" -> "음… 피곤하면 인지 기능 저하돼. 쉬는 게 맞아.";
            case "INTJ" -> "피곤하면 판단력 흐려져. 휴식 취해.";
            case "ISFP" -> "많이 피곤해 보여… 편하게 쉬어…";
            case "ISFJ" -> "고생했어… 이제 좀 쉬어도 돼!";
            case "ISTP" -> "피곤하면 그냥 자. 단순해.";
            case "ISTJ" -> "피곤할 땐 쉬는 게 맞아. 건강 챙겨.";
            default -> "많이 피곤했나 봐… 쉬어!";
        };
    }

    private String boredReply(String m) {
        return switch (m) {
            case "ENFP" -> "심심해?? 나랑 놀자!! 😆 뭐하고 싶어?";
            case "ENFJ" -> "심심하구나~ 뭐 재밌는 거 같이 해볼까?";
            case "ENTP" -> "심심? 뭐 재밌는 거 생각해볼까?";
            case "ENTJ" -> "심심하면 생산적인 거라도 해봐.";
            case "ESFP" -> "심심해?? 나가자!! 뭐라도 하자!! 😄";
            case "ESFJ" -> "심심해? 우리 같이 수다 떨까?";
            case "ESTP" -> "심심? 뭐라도 재밌는 거 찾아보자!";
            case "ESTJ" -> "심심하면 할 일 목록 확인해봐.";
            case "INFP" -> "심심할 땐… 음악 들으면 어때?";
            case "INFJ" -> "심심하구나. 뭔가 의미 있는 거 해볼래?";
            case "INTP" -> "심심? 뭐 궁금한 거라도 찾아봐.";
            case "INTJ" -> "심심하면 공부라도 해. 시간 낭비 말고.";
            case "ISFP" -> "심심해? 그림 그리거나 음악 들어봐…";
            case "ISFJ" -> "심심해? 뭐 편한 거 하면서 쉴래?";
            case "ISTP" -> "심심? 뭐라도 만져보든가.";
            case "ISTJ" -> "심심하면 정리할 거 찾아봐.";
            default -> "심심해? 뭐하고 싶어?";
        };
    }

    private String workReply(String m) {
        return switch (m) {
            case "ENFP" -> "공부/일?? 화이팅!! 😆 힘들면 쉬엄쉬엄 해!";
            case "ENFJ" -> "열심히 하는구나! 응원할게! 힘내!";
            case "ENTP" -> "공부/일 중? 효율적으로 하고 있어?";
            case "ENTJ" -> "좋아. 계획대로 진행하고 있어?";
            case "ESFP" -> "공부/일 하느라 고생이다!! 파이팅!! 😄";
            case "ESFJ" -> "열심히 하는구나~ 응원해! 힘내!";
            case "ESTP" -> "일 중? 빨리 끝내고 놀자!";
            case "ESTJ" -> "좋아. 집중해서 끝내자.";
            case "INFP" -> "힘들면 잠깐씩 쉬면서 해… 무리하지 마.";
            case "INFJ" -> "열심히 하는구나. 의미 있는 일이야?";
            case "INTP" -> "음… 효율적으로 하고 있어?";
            case "INTJ" -> "집중해서 목표 달성해. 잘하고 있어.";
            case "ISFP" -> "공부/일 힘들지…? 쉬면서 해…";
            case "ISFJ" -> "고생 많아… 힘들면 말해줘!";
            case "ISTP" -> "일 중? 집중해서 끝내.";
            case "ISTJ" -> "좋아. 계획대로 하면 돼.";
            default -> "열심히 하는구나! 힘내!";
        };
    }

    private String exerciseReply(String m) {
        return switch (m) {
            case "ENFP" -> "운동?? 나도 같이 할래!! 😆 뭐해?";
            case "ENFJ" -> "운동 좋지! 건강 챙기는 거 중요해!";
            case "ENTP" -> "운동? 어떤 거 하는데?";
            case "ENTJ" -> "좋아. 꾸준히 하는 게 중요해.";
            case "ESFP" -> "운동!! 재밌겠다!! 나도 끼워줘!! 😄";
            case "ESFJ" -> "운동 하는구나! 건강 챙기는 거 좋아!";
            case "ESTP" -> "운동? 좋지! 뭐하는데?";
            case "ESTJ" -> "운동은 규칙적으로 해야 효과 있어.";
            case "INFP" -> "운동… 몸 관리하는구나. 좋아 보여.";
            case "INFJ" -> "운동으로 스트레스 푸는 것도 좋아.";
            case "INTP" -> "운동? 건강에 도움 되긴 하지.";
            case "INTJ" -> "운동 계획 잘 세워서 하고 있어?";
            case "ISFP" -> "운동하면 기분 좋아지지…";
            case "ISFJ" -> "운동 하는구나! 무리하지는 마!";
            case "ISTP" -> "운동? 뭐하는데?";
            case "ISTJ" -> "꾸준한 운동 좋아. 계속해.";
            default -> "운동 좋지! 뭐해?";
        };
    }

    private String decisionReply(String m) {
        return switch (m) {
            case "ENFP" -> "고민돼?? 같이 생각해보자!! 뭐 때문에 그래? 😮";
            case "ENFJ" -> "고민이구나… 내가 같이 고민해줄게. 말해봐.";
            case "ENTP" -> "선택? 각 옵션의 장단점부터 보자!";
            case "ENTJ" -> "고민? 목표 기준으로 판단해. 뭐가 최선이야?";
            case "ESFP" -> "고민돼?? 내 생각 들어볼래? 같이 생각해보자!";
            case "ESFJ" -> "고민 있어? 내가 도와줄게! 말해봐!";
            case "ESTP" -> "고민? 직관 믿고 빠르게 결정해!";
            case "ESTJ" -> "고민? 조건 정리해서 논리적으로 판단해.";
            case "INFP" -> "고민스럽구나… 네 마음이 끌리는 쪽으로 가는 게 어때?";
            case "INFJ" -> "고민이 있어? 네 내면의 소리 들어봐. 뭐가 맞는 것 같아?";
            case "INTP" -> "고민? 각 선택지 분석해보자. 논리적으로.";
            case "INTJ" -> "결정 고민? 장기적 관점에서 분석해봐.";
            case "ISFP" -> "고민돼? 네 감정 따라가는 게 어때…";
            case "ISFJ" -> "고민 있구나… 내가 도와줄 수 있어? 말해줘.";
            case "ISTP" -> "고민? 실용적인 걸로 골라.";
            case "ISTJ" -> "고민? 신중하게 분석해서 결정해.";
            default -> "고민이구나… 같이 생각해보자!";
        };
    }

    // ----------------- 기존 답변 메소드들 -----------------
    
    private String emotionReply(String m) {
        return switch (m) {
            case "INFP" -> "음… 너 요즘 마음이 조금 예민해진 것 같아. 이야기해줄래? 내가 들어줄게.";
            case "INFJ" -> "네 감정에는 늘 이유가 있어. 천천히 말해줘, 난 괜찮아.";
            case "ENFP" -> "헉 😮 무슨 일 있었던 거야?? 나 완전 집중 모드 ON이야!";
            case "ENFJ" -> "오늘 기분이 조금 힘들었나 보네. 괜찮아, 내가 같이 생각해볼게.";
            case "INTJ" -> "감정이라는 건 원인을 분석해야 해. 어떤 상황이었는지 말해줄래?";
            case "INTP" -> "음... 기분이 왜 그런지 원인을 찾아보는 게 좋을 듯. 말해볼래?";
            case "ISFJ" -> "많이 힘들었겠다… 얘기해주면 내가 들어줄게.";
            case "ISFP" -> "괜찮아… 오늘 많이 힘들었지? 말하고 싶으면 말해줘.";
            case "ISTJ" -> "기분이 안 좋은 데에는 원인이 있을 거야. 말해줘, 해결하고 싶어.";
            case "ISTP" -> "음… 기분이 별로야? 무슨 일 있었어?";
            case "ESFP" -> "기분 별로면 나랑 수다 떨자! 금방 좋아질걸?? 😄";
            case "ESFJ" -> "괜찮아? 너 표정이 안 좋아 보여… 무슨 일 있었어?";
            case "ESTP" -> "기분 별로야? 가자, 뭐라도 재밌는 거 하면서 털어버리자!";
            case "ESTJ" -> "기분 조절은 어렵지. 어떤 일 때문에 그런지 말해볼래?";
            case "ENTP" -> "기분? 오호, 흥미롭네. 어떤 사건이었길래 그래?";
            case "ENTJ" -> "감정 기복은 원인 관리가 우선이야. 원인이 뭐였어?";
            default -> "기분이 안 좋았던 거야? 괜찮아, 말해줘.";
        };
    }

    private String comfortReply(String m) {
        return switch (m) {
            case "INFP" -> "아… 그거 진짜 마음 아팠겠다… 너 그러려고 그런 게 아니야.";
            case "INFJ" -> "네가 그런 감정 느끼는 건 너무 자연스러워. 너 잘하고 있어.";
            case "ISFP" -> "오늘 정말 애썼어… 따뜻한 거 마시면서 조금 쉬자.";
            case "ISFJ" -> "많이 힘들었겠다… 네 얘기 들으면 내가 더 마음 아파.";
            case "ENFP" -> "으악 😢 누가 너한테 그런 스트레스를 줘?? 내가 혼내줄까?!";
            case "ENFJ" -> "힘들었겠다. 근데 너 진짜 잘 견뎠어. 난 네 편이야.";
            case "ESFJ" -> "고생 많았다… 지금은 네가 좀 편안해졌으면 좋겠다.";
            case "ESFP" -> "아이고 ㅠㅠ 힘들었겠다! 우리 맛있는 거 먹고 풀자!";
            case "INTJ" -> "음… 원인을 제거하는 게 중요해. 어떤 부분이 제일 스트레스였어?";
            case "INTP" -> "복잡했겠다. 차분히 원인을 분석하면 해결될 거야.";
            case "ISTJ" -> "고생했네. 이런 일은 누구에게나 올 수 있어.";
            case "ISTP" -> "힘든 날이네. 잠깐 쉬면 괜찮아질걸.";
            case "ENTJ" -> "힘들었어도 잘 버텼어. 다음엔 더 잘할 수 있을 거야.";
            case "ENTP" -> "그거 스트레스였겠다. 근데 좀 재미있게(?) 해결할 방법 없나?";
            case "ESTP" -> "에이 힘들었네. 나가서 좀 걸을래? 금방 나아질걸.";
            case "ESTJ" -> "수고 많았다. 문제는 해결하면 그만이야. 같이 생각해보자.";
            default -> "고생 많았어… 조금 쉬어도 괜찮아.";
        };
    }

    private String foodReply(String m) {
        return switch (m) {
            case "ENFP" -> "밥!!! 😆 뭐 먹고 싶어? 맛있는 얘기만 해도 기분 좋아져~";
            case "ESFP" -> "헐 뭐 먹을지 고민 중?? 맛있는 거 먹자!! 🍔🍜";
            case "ISFJ" -> "따뜻한 음식 먹으면 기분 좋아질 텐데… 뭐 먹고 싶어?";
            case "INFP" -> "따뜻한 거 먹으면 마음도 좀 편해질 거야. 뭐 먹고 싶어?";
            case "INTJ" -> "영양 균형을 생각해야 해. 지금 생각나는 메뉴 있어?";
            case "ISTJ" -> "식사 시간인가 보네. 뭘 먹을지 정했어?";
            case "ISTP" -> "뭐 먹고 싶은데? 간단한 게 좋지 않아?";
            case "ISFP" -> "음식 얘기만 해도 행복하다… 뭐 먹고 싶어?";
            case "ENTP" -> "밥? 새로운 메뉴 도전해볼래??";
            case "ENTJ" -> "먹어야 에너지가 나지. 뭐 먹을지 정해봐.";
            case "ENFJ" -> "너 오늘 힘들었으니까 맛있는 거 먹자. 뭐 먹고 싶어?";
            case "ESFJ" -> "뭐 먹고 싶어? 같이 먹으면 더 맛있잖아!";
            case "ESTP" -> "오케이 먹자. 뭐든 좋아 보이는데?";
            case "ESTJ" -> "밥은 중요하지. 메뉴 골랐어?";
            case "INTP" -> "음… 뭐가 제일 효율적이지? 간단한 게 좋겠다.";
            default -> "뭐 먹고 싶어?";
        };
    }

    private String activityReply(String m) {
        return switch (m) {
            case "ISTP" -> "그냥 이것저것 하고 있었어. 너는 뭐해?";
            case "ISTJ" -> "정리 좀 하고 있었어. 너는 지금 뭐하는 중이야?";
            case "INTJ" -> "계획 정리하다가 쉬는 중. 넌?";
            case "INTP" -> "음… 그냥 생각 중이었어. 너는?";
            case "INFP" -> "그냥 쉬고 있었어. 너는 뭐해? 궁금하다…";
            case "ISFP" -> "조용히 쉬는 중이었어. 너는?";
            case "INFJ" -> "내 시간 보내고 있었어. 너는 지금 뭐하고 있어?";
            case "ISFJ" -> "집에서 쉬다가 너 메시지 받아서 기분 좋아졌어.";
            case "ENFP" -> "나?? 너 생각 ㅋㅋㅋ 장난이고 뭐해?? 😆";
            case "ENFJ" -> "지금은 괜찮아~ 넌 뭐하고 있었어?";
            case "ENTP" -> "재밌는 거 찾는 중이었지! 너는 뭔데?";
            case "ENTJ" -> "일 잠깐 멈추고 쉬는 중. 넌 뭘 하고 있었어?";
            case "ESTP" -> "심심해서 뭐라도 하려던 참이었어. 너는?";
            case "ESTJ" -> "정리 중이었어. 너는 지금 뭐해?";
            case "ESFP" -> "심심해서 폰 만지는 중~ 너는??";
            case "ESFJ" -> "집에서 쉬고 있었지! 넌 뭐해?";
            default -> "응, 너는 뭐하고 있어?";
        };
    }

    private String scheduleReply(String m) {
        return switch (m) {
            case "INTJ" -> "일정 관리는 중요하지. 내일 어느 시간 말하는 거야?";
            case "ISTJ" -> "내일 일정 체크해볼까? 어느 시간대 말하는 거야?";
            case "INFJ" -> "내일? 음… 너는 일정 괜찮아?";
            case "INFP" -> "내일… 뭐 좋은 계획이라도 있어?";
            case "INTP" -> "내일 일정… 음 아직 안 정함.";
            case "ISTP" -> "내일? 상황 봐야 할 듯.";
            case "ISFJ" -> "내일 일정 괜찮아! 너는?";
            case "ISFP" -> "내일? 음… 특별한 건 없을 듯.";
            case "ENTJ" -> "내일? 시간 말하면 그때 맞춰볼게.";
            case "ENTP" -> "내일 뭐 재밌는 계획 있어??";
            case "ENFJ" -> "내일 시간 괜찮아! 너는?";
            case "ENFP" -> "오 내일?? 뭐 좋은 일 생기나?? 😆";
            case "ESFJ" -> "내일 괜찮아~ 너는?";
            case "ESFP" -> "내일?? 즐거운 일 있나봐?!";
            case "ESTP" -> "오케이 내일 보자고?";
            case "ESTJ" -> "내일 일정? 알려주면 맞춰볼게.";
            default -> "내일 일정 말하는 거지?";
        };
    }

    private String genericReply(String m) {
        return switch (m) {
            case "ENFP" -> "와 재밌다 ㅋㅋㅋ 더 얘기해봐 😄";
            case "ENFJ" -> "응응~ 그 얘기 자세히 말해줄래?";
            case "ENTP" -> "오옼ㅋㅋ 그건 좀 흥미로운데?";
            case "ENTJ" -> "좋아. 핵심이 뭐야?";
            case "ESFP" -> "꺄 재밌다!! 더 말해봐! 😆";
            case "ESFJ" -> "응응~ 난 듣는 중! 계속 말해줘.";
            case "ESTP" -> "오 좋은데? 계속 말해봐!";
            case "ESTJ" -> "오케이. 그 다음은?";
            case "INTJ" -> "흥미롭네. 좀 더 설명해줄래?";
            case "INTP" -> "음… 이해되는 듯. 계속 얘기해봐.";
            case "INFJ" -> "그 말 속에 감정이 좀 느껴지네. 이어서 말해줘.";
            case "INFP" -> "오… 뭔가 느낌 있다. 계속 얘기해줘!";
            case "ISTP" -> "음. 계속해봐.";
            case "ISTJ" -> "알겠어. 다음은?";
            case "ISFP" -> "응응… 듣고 있어. 말해줘.";
            case "ISFJ" -> "그렇구나… 이어서 말할래?";
            default -> "응응~ 계속 얘기해줘!";
        };
    }
}