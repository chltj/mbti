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

    @GetMapping("/")
    public String index() { 
        return "Main"; 
    }

    @GetMapping("/mbti")
    public String mbtiPage() { 
        return "mbti"; 
    }

    @PostMapping("/mbti")
    public String analyze(@RequestParam("file") MultipartFile file,
                          @RequestParam("myName") String myName,
                          Model model) {
        try {
            if (file.isEmpty()) {
                model.addAttribute("error", "카카오톡 txt 파일을 업로드해 주세요.");
                return "mbti";
            }

            String rawText = new String(file.getBytes(), StandardCharsets.UTF_8);
            String targetName = myName.trim();

            // 1. 파싱 (이름별 대화 내용 분리)
            Map<String, List<String>> users = KakaoParser.parseByUser(rawText);

            // 2. 내가 입력한 이름 존재 여부 확인
            if (!users.containsKey(targetName)) {
                model.addAttribute("error", "이름을 찾을 수 없습니다. (발견된 이름: " + users.keySet() + ")");
                return "mbti";
            }

            // 3. 나(ME) MBTI 분석
            List<String> myMessages = users.get(targetName);
            HybridMbtiResult myResult = safeAnalyze(myMessages);

            // 4. 상대방들(단체톡이면 여러 명)을 모두 분석
            List<PartnerView> partnerResults = new ArrayList<>();

            for (Map.Entry<String, List<String>> entry : users.entrySet()) {
                String name = entry.getKey();
                if (name.equals(targetName)) continue; // 내 자신은 제외

                List<String> msgs = entry.getValue();
                HybridMbtiResult partnerResult = safeAnalyze(msgs);

                int chemistryScore = calcScore(myResult.getFinalMbti(), partnerResult.getFinalMbti());

                PartnerView view = new PartnerView(name, partnerResult, chemistryScore);
                partnerResults.add(view);
            }

            // 5. 모델에 담아서 뷰로 전달
            model.addAttribute("myName", targetName);
            model.addAttribute("myResult", myResult);

            // ✔ 단체톡용 : 여러 명의 상대 결과 리스트
            model.addAttribute("partnerResults", partnerResults);

            // ✔ 기존 단일 상대용(호환용): 첫 번째 상대만 사용
            if (!partnerResults.isEmpty()) {
                PartnerView first = partnerResults.get(0);
                model.addAttribute("partnerName", first.getName());
                model.addAttribute("partnerResult", first.getResult());
                model.addAttribute("chemistryScore", first.getChemistryScore());
            } else {
                model.addAttribute("partnerName", "상대방");
                model.addAttribute("partnerResult", null);
                model.addAttribute("chemistryScore", 0);
            }

            return "result";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "분석 중 오류가 발생했습니다.");
            return "mbti";
        }
    }

    // 🔹 MBTI 하이브리드 분석 (안전 래핑)
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
            e.printStackTrace();
            return new HybridMbtiResult("ERROR", 0.0, "-", "-");
        }
    }

    // 🔹 궁합 점수 계산
    private int calcScore(String m1, String m2) {
        if (m1 == null || m2 == null) return 0;
        if (m1.equals("UNKNOWN") || m2.equals("UNKNOWN")) return 0;
        if (m1.equals("ERROR") || m2.equals("ERROR")) return 0;

        int score = 70;
        if (m1.equals(m2)) return 95;
        if (m1.charAt(0) != m2.charAt(0)) score += 10;
        if (m1.charAt(3) != m2.charAt(3)) score += 5;
        return Math.min(score, 100);
    }

    // 🔹 뷰에서 쓰기 좋은 작은 DTO
    public static class PartnerView {
        private String name;
        private HybridMbtiResult result;
        private int chemistryScore;

        public PartnerView(String name, HybridMbtiResult result, int chemistryScore) {
            this.name = name;
            this.result = result;
            this.chemistryScore = chemistryScore;
        }

        public String getName() {
            return name;
        }

        public HybridMbtiResult getResult() {
            return result;
        }

        public int getChemistryScore() {
            return chemistryScore;
        }
    }
}
    