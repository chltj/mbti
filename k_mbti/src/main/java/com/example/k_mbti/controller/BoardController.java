package com.example.k_mbti.controller;

import com.example.k_mbti.dto.InquiryDto;
import com.example.k_mbti.dto.UserDto;
import com.example.k_mbti.service.InquiryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final InquiryService inquiryService;

    public BoardController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    /** 문의 게시판 목록 */
    @GetMapping("/inquiry")
    public String inquiryList(Model model) {
        List<InquiryDto> list = inquiryService.getInquiryList();
        model.addAttribute("inquiryList", list);
        return "board/inquiry";   // templates/board/inquiry.html
    }

    /** 문의 작성 폼 */
    @GetMapping("/inquiry/write")
    public String inquiryWriteForm(Model model) {
        model.addAttribute("inquiry", new InquiryDto());
        return "board/write";
    }

    /** 문의 작성 처리 */
    @PostMapping("/inquiry/write")
    public String inquiryWrite(@ModelAttribute InquiryDto inquiry,
                               HttpSession session) {

        // 세션에서 로그인 유저 가져오기
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if (loginUser != null) {
            inquiry.setWriter(loginUser.getNickname()); // 또는 getEmail()
        } else {
            inquiry.setWriter("익명");
        }

        inquiryService.writeInquiry(inquiry);
        return "redirect:/board/inquiry";
    }

    /** 문의 상세 보기 (선택) */
    @GetMapping("/inquiry/{id}")
    public String inquiryDetail(@PathVariable Long id, Model model) {
        InquiryDto inquiry = inquiryService.getInquiry(id);
        model.addAttribute("inquiry", inquiry);
        return "board/inquiry-detail";
    }
}
