package com.example.k_mbti.controller;

import com.example.k_mbti.dto.InquiryDto;
import com.example.k_mbti.dto.UserDto;
import com.example.k_mbti.service.InquiryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            inquiry.setWriter(loginUser.getNickname()); // 닉네임 기준
        } else {
            inquiry.setWriter("익명");
        }

        inquiryService.writeInquiry(inquiry);
        return "redirect:/board/inquiry";
    }


   /** 문의 상세 보기 */
@GetMapping("/inquiry/{id}")
public String detail(@PathVariable Long id,
                     HttpSession session,
                     Model model,
                     RedirectAttributes rttr) {

    InquiryDto inquiry = inquiryService.findById(id);
    if (inquiry == null) {
        rttr.addFlashAttribute("errorMsg", "해당 문의를 찾을 수 없습니다.");
        return "redirect:/board/inquiry";   // 목록으로
    }

    UserDto loginUser = (UserDto) session.getAttribute("loginUser");

    // 🔐 로그인 안 했거나, 내 글이 아니면 → 상세 페이지 진입 차단
    if (loginUser == null ||
        inquiry.getWriter() == null ||
        !inquiry.getWriter().equals(loginUser.getNickname())) {

        rttr.addFlashAttribute("errorMsg", "접근 권한이 없습니다.");
        return "redirect:/board/inquiry";   // 목록으로 돌려보냄
    }

    // ✅ 여기까지 온 경우: 내 글 → 상세 페이지 진입 허용
    boolean canEdit = true; // 내 글이니 수정 가능
    model.addAttribute("inquiry", inquiry);
    model.addAttribute("canEdit", canEdit);

    return "board/detail"; // 상세 페이지 템플릿
}

    /** 문의 수정 폼 */
   // ✏ 문의 수정 폼
@GetMapping("/inquiry/{id}/edit")
public String editForm(@PathVariable Long id,
                       HttpSession session,
                       Model model,
                       RedirectAttributes rttr) {

    InquiryDto inquiry = inquiryService.findById(id);
    if (inquiry == null) {
        rttr.addFlashAttribute("errorMsg", "해당 문의를 찾을 수 없습니다.");
        return "redirect:/board/inquiry";   // 목록으로 돌려보냄
    }

    UserDto loginUser = (UserDto) session.getAttribute("loginUser");

    // 🔐 로그인 안 했거나, 내 글이 아니면 -> 수정 페이지 진입 자체 차단
    if (loginUser == null ||
        inquiry.getWriter() == null ||
        !inquiry.getWriter().equals(loginUser.getNickname())) {

        rttr.addFlashAttribute("errorMsg", "접근 권한이 없습니다.");
        return "redirect:/board/inquiry/" + id;   // 상세 페이지로 다시 보내기
    }

    // ✅ 여기까지 온 사람만 진짜 수정 화면으로
    model.addAttribute("inquiry", inquiry);
    return "board/edit";
}


 @PostMapping("/inquiry/{id}/edit")
public String edit(@PathVariable Long id,
                   @ModelAttribute InquiryDto form,
                   HttpSession session,
                   RedirectAttributes rttr) {

    InquiryDto inquiry = inquiryService.findById(id);
    if (inquiry == null) {
        rttr.addFlashAttribute("errorMsg", "해당 문의를 찾을 수 없습니다.");
        return "redirect:/board/inquiry";
    }

    UserDto loginUser = (UserDto) session.getAttribute("loginUser");

    if (loginUser == null ||
        inquiry.getWriter() == null ||
        !inquiry.getWriter().equals(loginUser.getNickname())) {

        rttr.addFlashAttribute("errorMsg", "접근 권한이 없습니다.");
        return "redirect:/board/inquiry/" + id;
    }

    // ✅ 실제 수정
    inquiry.setTitle(form.getTitle());
    inquiry.setContent(form.getContent());
    inquiryService.updateInquiry(inquiry);

    return "redirect:/board/inquiry/" + id;
}

}
