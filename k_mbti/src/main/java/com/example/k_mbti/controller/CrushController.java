package com.example.k_mbti.controller;

import com.example.k_mbti.dto.CrushResultDto;
import com.example.k_mbti.service.CrushService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
                               Model model) throws IOException {

        if (file.isEmpty()) {
            model.addAttribute("error", "카카오톡 txt 파일을 업로드해 주세요.");
            return "crush";
        }

        String text = new String(file.getBytes(), StandardCharsets.UTF_8);

        if (!text.contains(myName)) {
            model.addAttribute("error",
                    "카톡 내보내기 파일 안에서 '" + myName + "' 을(를) 찾지 못했어요. 대화명을 다시 확인해 주세요.");
            return "crush";
        }

        if (!text.contains(targetName)) {
            model.addAttribute("error",
                    "카톡 내보내기 파일 안에서 '" + targetName + "' 을(를) 찾지 못했어요. 1:1 대화인지, 대화명을 확인해 주세요.");
            return "crush";
        }

        CrushResultDto result = crushService.analyze(text, myName, targetName, range);

        model.addAttribute("myName", myName);
        model.addAttribute("targetName", targetName);
        model.addAttribute("crushScore", result.getScore());
        model.addAttribute("crushLevel", result.getLevel());
        model.addAttribute("crushComment", result.getComment());
        model.addAttribute("replySpeedLabel", result.getReplySpeedLabel());
        model.addAttribute("frequencyLabel", result.getFrequencyLabel());
        model.addAttribute("initiativeLabel", result.getInitiativeLabel());
        model.addAttribute("warmthLabel", result.getWarmthLabel());

        return "crush-result"; // templates/crush-result.html
    }
}
