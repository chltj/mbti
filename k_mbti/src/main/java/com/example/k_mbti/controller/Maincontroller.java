package com.example.k_mbti.controller;

import com.example.k_mbti.parser.KakaoParser;
import com.example.k_mbti.service.MbtiRuleService;
import com.example.k_mbti.mbti.hybrid.MbtiHybridService;
import com.example.k_mbti.mbti.hybrid.HybridMbtiResult;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Controller
public class Maincontroller {

    private final MbtiRuleService mbtiRuleService;
    private final MbtiHybridService mbtiHybridService;

    public Maincontroller(MbtiRuleService mbtiRuleService,
                          MbtiHybridService mbtiHybridService) {
        this.mbtiRuleService = mbtiRuleService;
        this.mbtiHybridService = mbtiHybridService;
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
    public String analyze(
            @RequestParam("file") MultipartFile file,
            @RequestParam("myName") String myName,
            Model model) {

        try {

            if (file.isEmpty()) {
                model.addAttribute("error", "파일이 업로드되지 않았습니다.");
                return "Mbti";
            }

            myName = myName.trim();

            String rawText = new String(file.getBytes(), StandardCharsets.UTF_8);
            Map<String, List<String>> userMessages = KakaoParser.parseByUser(rawText);

            if (!userMessages.containsKey(myName)) {
                model.addAttribute(
                        "error",
                        "카톡 파일에서 '" + myName + "' 을(를) 찾을 수 없습니다.\n" +
                        "파싱된 이름: " + userMessages.keySet()
                );
                return "Mbti";
            }

            List<String> myMessages = userMessages.get(myName);

            // ✅ 규칙 MBTI
            String ruleMbti = mbtiRuleService.estimateMbti(myMessages);
            double ruleScore = 0.75;

            // ✅ ML 입력 텍스트
            String fullText = String.join(" ", myMessages);

            // ✅ 하이브리드 결과
            HybridMbtiResult finalResult =
                    mbtiHybridService.merge(ruleMbti, ruleScore, fullText);

            // ✅ 최종 UI 전달 데이터
            model.addAttribute("name", myName);
            model.addAttribute("mbti", finalResult.getFinalMbti());
            model.addAttribute("confidence",
                    (int) (finalResult.getFinalConfidence() * 100));
            model.addAttribute("ruleMbti", finalResult.getRuleMbti());
            model.addAttribute("mlMbti", finalResult.getMlMbti());

            return "result";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "에러 발생: " + e.getMessage());
            return "Mbti";
        }
    }
}
