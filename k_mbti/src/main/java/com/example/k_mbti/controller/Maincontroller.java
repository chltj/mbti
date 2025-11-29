package com.example.k_mbti.controller;

import com.example.k_mbti.parser.KakaoParser;
import com.example.k_mbti.service.MbtiRuleService;

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

    @GetMapping("/")
    public String index() {
        return "Main";  // Main.html을 의미
    }

    MbtiRuleService mbtiRuleService = new MbtiRuleService();

        @GetMapping("/mbti")
    public String mbtiPage() {
        return "mbti";  // Main.html을 의미
    }

   @PostMapping("/mbti")
    public String analyze(
            @RequestParam("file") MultipartFile file,
            @RequestParam("myName") String myName,
            Model model) {

        try {
            String rawText = new String(file.getBytes(), StandardCharsets.UTF_8);
            Map<String, List<String>> userMessages = KakaoParser.parseByUser(rawText);

            if (!userMessages.containsKey(myName)) {
                model.addAttribute("error", "카톡 파일에서 '" + myName + "' 을 찾을 수 없습니다!");
                return "Mbti";
            }

            List<String> myMessages = userMessages.get(myName);
            String mbti = mbtiRuleService.estimateMbti(myMessages);

            model.addAttribute("name", myName);
            model.addAttribute("mbti", mbti);

            return "result";

        } catch (Exception e) {
            model.addAttribute("error", "에러 발생: " + e.getMessage());
            return "Mbti";
        }
    }
    
}
