package com.example.k_mbti.controller;

import com.example.k_mbti.dto.CrushResultDto;
import com.example.k_mbti.parser.KakaoParser;
import com.example.k_mbti.service.MbtiRuleService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class Maincontroller {

    @GetMapping("/")
    public String index() {
        return "Main";
    }

    MbtiRuleService mbtiRuleService = new MbtiRuleService();

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

            // 이름 공백 제거
            myName = myName.trim();

            String rawText = new String(file.getBytes(), StandardCharsets.UTF_8);
            Map<String, List<String>> userMessages = KakaoParser.parseByUser(rawText);

            // ⭐ 디버깅용: 콘솔에 출력
            System.out.println("[DEBUG] myName = '" + myName + "'");
            System.out.println("[DEBUG] 파싱된 이름들 = " + userMessages.keySet());

            if (!userMessages.containsKey(myName)) {
                model.addAttribute(
                        "error",
                        "카톡 파일에서 '" + myName + "' 을(를) 찾을 수 없습니다.\n" +
                        "파싱된 이름: " + userMessages.keySet()
                );
                return "Mbti";
            }

            List<String> myMessages = userMessages.get(myName);
            String mbti = mbtiRuleService.estimateMbti(myMessages);

            model.addAttribute("name", myName);
            model.addAttribute("mbti", mbti);

            return "result";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "에러 발생: " + e.getMessage());
            return "Mbti";
        }
    }
}

