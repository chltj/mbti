package com.example.k_mbti.controller;

import com.example.k_mbti.parser.KakaoParser;
import com.example.k_mbti.service.MbtiRuleService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class Maincontroller {

    @Autowired
    private MbtiRuleService mbtiRuleService;

    @GetMapping("/")
    public String index() {
        return "Main";
    }

    @GetMapping("/mbti")
    public String mbtiPage() {
        return "mbti"; // mbti.html로 이동
    }
    
    // 파일 읽기 및 유효성 검사 헬퍼 메서드
    private String readAndValidateFile(MultipartFile file, Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("error", "파일이 업로드되지 않았습니다.");
            return null;
        }
        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }

    // ----------------------------------------------------
    // 1. 👥 단체 톡방 분석 로직 (모든 참여자 MBTI 분석)
    //    Endpoint: /mbti/group
    // ----------------------------------------------------
    @PostMapping("/mbti/group")
    public String analyzeGroup(
            @RequestParam("file") MultipartFile file,
            @RequestParam("myName") String myName, 
            Model model) {

        try {
            // 파일 유효성 검사 및 읽기
            String rawText = readAndValidateFile(file, model);
            if (rawText == null) return "Mbti";

            // 1. 파서에서 모든 참여자의 메시지 맵을 가져옴
            Map<String, List<String>> talkDataByUser = KakaoParser.parseByUser(rawText);

            // 2. 모든 참여자의 MBTI 분석 실행
            Map<String, String> allMbtiResults = mbtiRuleService.analyzeAllUsers(talkDataByUser);

            // 3. 결과를 모델에 담아 View로 전달
            model.addAttribute("allMbtiResults", allMbtiResults);
            model.addAttribute("analysisMode", "group"); // View에서 모드 구분용
            
            return "result"; 

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "분석 중 에러 발생: " + e.getMessage());
            return "Mbti";
        }
    }


    // ----------------------------------------------------
    // 2. 🙋‍♀️ 개인 톡방 분석 로직 (나와 상대방 MBTI 분석 + 궁합)
    //    Endpoint: /mbti/single
    // ----------------------------------------------------
    @PostMapping("/mbti/single")
    public String analyzeSingle(
            @RequestParam("file") MultipartFile file,
            @RequestParam("myName") String myName, 
            @RequestParam("targetName") String targetName, // 상대방 이름 추가
            Model model) {

        try {
            // 파일 유효성 검사 및 읽기
            String rawText = readAndValidateFile(file, model);
            if (rawText == null) return "Mbti";

            // 1. 파서에서 모든 참여자의 메시지 맵을 가져옴
            Map<String, List<String>> talkDataByUser = KakaoParser.parseByUser(rawText);
            
            // 2. 나와 상대방의 MBTI만 분석
            String myMbti = mbtiRuleService.estimateMbti(talkDataByUser.getOrDefault(myName, List.of()));
            String targetMbti = mbtiRuleService.estimateMbti(talkDataByUser.getOrDefault(targetName, List.of()));

            // 3. (옵션) 궁합 분석 로직이 있다면 여기서 호출
            // CrushResultDto crushResult = crushService.analyzeCrush(myMbti, targetMbti);

            // 4. 결과를 모델에 담아 View로 전달
            model.addAttribute("myMbti", myMbti);
            model.addAttribute("targetMbti", targetMbti);
            model.addAttribute("myName", myName);
            model.addAttribute("targetName", targetName);
            model.addAttribute("analysisMode", "single"); // View에서 모드 구분용
            // model.addAttribute("crushResult", crushResult);
            
            return "result"; 

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "분석 중 에러 발생: " + e.getMessage());
            return "Mbti";
        }
    }
}