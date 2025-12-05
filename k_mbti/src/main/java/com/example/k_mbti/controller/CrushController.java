package com.example.k_mbti.controller;

import com.example.k_mbti.dto.CrushResultDto;
import com.example.k_mbti.service.CrushService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@Controller
public class CrushController {

    private final CrushService crushService;

    public CrushController(CrushService crushService) {
        this.crushService = crushService;
    }

    // 호감도 업로드 페이지 열기
    @GetMapping("/crush")
    public String showCrushPage() {
        return "crush"; // templates/crush.html
    }

    // 호감도 분석
    @PostMapping("/crush")
    public String analyzeCrush(@RequestParam String myName,
                               @RequestParam String targetName,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam String range,
                               Model model) {

        if (file.isEmpty()) {
            model.addAttribute("error", "카카오톡 txt 파일을 업로드해 주세요.");
            return "crush";
        }

        try {
            // 1. 파일 내용을 UTF-8로 읽음 (IOException 발생 가능)
            String text = new String(file.getBytes(), StandardCharsets.UTF_8);

            // 2. 대화명 유효성 검사 (파일 안에 대화명이 있는지 확인)
            if (!text.contains(myName)) {
                model.addAttribute("error", "카톡 내보내기 파일 안에서 '" + myName + "' 을(를) 찾지 못했어요. 대화명을 다시 확인해 주세요.");
                return "crush";
            }

            if (!text.contains(targetName)) {
                model.addAttribute("error", "카톡 내보내기 파일 안에서 '" + targetName + "' 을(를) 찾지 못했어요. 1:1 대화인지, 대화명을 확인해 주세요.");
                return "crush";
            }
            
            // 3. 서비스 로직 호출 및 결과 받기
            CrushResultDto result = crushService.analyze(text, myName, targetName, range);

            // 4. 모델에 결과 DTO의 모든 필드 추가 (10개 필드)
            model.addAttribute("myName", myName);
            model.addAttribute("targetName", targetName);
            
            // 종합 정보 (점수 및 코멘트)
            model.addAttribute("crushScore", result.getScore());
            model.addAttribute("crushLevel", result.getLevel());
            model.addAttribute("crushComment", result.getComment());
            
            // 7가지 세부 지표 라벨
            model.addAttribute("replySpeedLabel", result.getReplySpeedLabel());
            model.addAttribute("frequencyLabel", result.getFrequencyLabel());
            model.addAttribute("initiativeLabel", result.getInitiativeLabel());
            model.addAttribute("warmthLabel", result.getWarmthLabel());
            model.addAttribute("lengthLabel", result.getLengthLabel());     // 확장된 DTO
            model.addAttribute("overlapLabel", result.getOverlapLabel());   // 확장된 DTO
            model.addAttribute("ratioLabel", result.getRatioLabel());       // 확장된 DTO

            return "crush-result"; // templates/crush-result.html
            
        } catch (Exception e) {
            // 파일 읽기 실패, 파일 파싱 중 치명적인 오류 등 모든 예외를 처리
            e.printStackTrace();
            model.addAttribute("error", "파일 처리 중 심각한 오류가 발생했습니다. 파일 형식을 확인해 주세요.");
            return "crush"; 
        }
    }
}