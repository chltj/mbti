package com.example.k_mbti.controller;

import com.example.k_mbti.parser.KakaoParser;
import com.example.k_mbti.service.MbtiRuleService;
import com.example.k_mbti.mbti.hybrid.MbtiHybridService;
import com.example.k_mbti.mbti.hybrid.HybridMbtiResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class Maincontroller {

    private final MbtiRuleService ruleService;
    private final MbtiHybridService hybridService;

    public Maincontroller(MbtiRuleService ruleService, MbtiHybridService hybridService) {
        this.ruleService = ruleService;
        this.hybridService = hybridService;
    }

    @GetMapping("/") public String index() { return "Main"; }
    @GetMapping("/mbti") public String mbtiPage() { return "mbti"; }

    @PostMapping("/mbti")
    public String analyze(@RequestParam("file") MultipartFile file, 
                          @RequestParam("myName") String myName, 
                          Model model) {
        try {
            if (file.isEmpty()) return "mbti";

            String rawText = new String(file.getBytes(), StandardCharsets.UTF_8);
            String targetName = myName.trim();

            // 1. 파싱 (호감도용 파서 사용)
            Map<String, List<String>> users = KakaoParser.parseByUser(rawText);

            // 이름 체크
            if (!users.containsKey(targetName)) {
                model.addAttribute("error", "이름을 찾을 수 없습니다. (발견된 이름: " + users.keySet() + ")");
                return "mbti";
            }

            // 2. 상대방 찾기
            String partnerName = users.keySet().stream()
                    .filter(n -> !n.equals(targetName))
                    .findFirst().orElse("상대방");

            // 3. 분석 실행 (안전하게)
            HybridMbtiResult myResult = safeAnalyze(users.get(targetName));
            HybridMbtiResult partnerResult = safeAnalyze(users.get(partnerName));

            // 4. 결과 전달
            model.addAttribute("myName", targetName);
            model.addAttribute("partnerName", partnerName);
            model.addAttribute("myResult", myResult);
            model.addAttribute("partnerResult", partnerResult);
            
            // 궁합 점수
            model.addAttribute("chemistryScore", calcScore(myResult.getFinalMbti(), partnerResult.getFinalMbti()));

            return "result"; 

        } catch (Exception e) {
            e.printStackTrace();
            return "mbti";
        }
    }

private HybridMbtiResult safeAnalyze(List<String> msgs) {
    if (msgs == null || msgs.isEmpty()) 
        return new HybridMbtiResult("UNKNOWN", 0.0, "-", "-");
    try {
        String ruleMbti = ruleService.estimateMbti(msgs);
        System.out.println("=== estimateMbti 결과 ===");
        System.out.println(ruleMbti);
        
        HybridMbtiResult merged = hybridService.merge(ruleMbti, 0.75, String.join(" ", msgs));
        System.out.println("=== 최종 Hybrid 결과 ===");
        System.out.println(merged);

        return merged;
    } catch (Exception e) {
        return new HybridMbtiResult("ERROR", 0.0, "-", "-");
    }
}


    private int calcScore(String m1, String m2) {
        if (m1.equals("UNKNOWN") || m2.equals("UNKNOWN")) return 0;
        int score = 70;
        if (m1.equals(m2)) return 95;
        if (m1.charAt(0) != m2.charAt(0)) score += 10;
        if (m1.charAt(3) != m2.charAt(3)) score += 5;
        return Math.min(score, 100);
    }

    
}
