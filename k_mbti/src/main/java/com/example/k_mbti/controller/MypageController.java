package com.example.k_mbti.controller;

import com.example.k_mbti.dto.ChatRoomDto;
import com.example.k_mbti.dto.InquiryDto;
import com.example.k_mbti.dto.UserDto;
import com.example.k_mbti.service.AuthService;
import com.example.k_mbti.service.ChatRoomService;
import com.example.k_mbti.service.InquiryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MypageController {

    private final ChatRoomService chatRoomService;
    private final InquiryService inquiryService;
    private final AuthService authService;

    public MypageController(ChatRoomService chatRoomService,
                            InquiryService inquiryService,
                            AuthService authService) {
        this.chatRoomService = chatRoomService;
        this.inquiryService = inquiryService;
        this.authService = authService;
    }

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {

        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            model.addAttribute("errorMsg", "로그인 후 이용 가능합니다.");
            return "login";
        }

        String myName = loginUser.getNickname();

        List<ChatRoomDto> myRooms = chatRoomService.getRoomsByMember(myName);

        List<InquiryDto> myInquiries =
                inquiryService.getInquiryList()
                        .stream()
                        .filter(inq -> myName.equals(inq.getWriter()))
                        .toList();

        model.addAttribute("myRooms", myRooms);
        model.addAttribute("myInquiries", myInquiries);
        model.addAttribute("loginUser", loginUser);

        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String editForm(HttpSession session, Model model) {

        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        UserDto auth = authService.findById(loginUser.getId());
        model.addAttribute("auth", auth);  // mypage-edit.html 의 th:object="${auth}"

        return "mypage-edit";
    }

    /** 내 정보 수정 처리 */
@PostMapping("/mypage/edit")
public String editSubmit(@ModelAttribute("auth") UserDto form,
                         @RequestParam(required = false) String newPassword,
                         @RequestParam(required = false) String newPasswordConfirm,
                         HttpSession session,
                         Model model) {

    UserDto loginUser = (UserDto) session.getAttribute("loginUser");
    if (loginUser == null) {
        return "redirect:/login";
    }

    // 항상 세션의 id를 사용 (보안 + 정확성)
    form.setId(loginUser.getId());

    String oldNickname = loginUser.getNickname();
    String newNickname = form.getNickname();

    // 🔍 여기서 한 번 form에 뭐가 들어왔는지 찍어보자
    System.out.println(
            "[MypageController.beforePw] id=" + form.getId()
                    + ", nickname=" + form.getNickname()
                    + ", email=" + form.getEmail()
                    + ", phone=" + form.getPhone()
                    + ", rawPassword=" + form.getPassword()
    );

    // 비밀번호 변경 의사가 있는 경우
    if ((newPassword != null && !newPassword.isBlank()) ||
        (newPasswordConfirm != null && !newPasswordConfirm.isBlank())) {

        if (newPassword == null || !newPassword.equals(newPasswordConfirm)) {
            model.addAttribute("auth", form);
            model.addAttribute("errorMsg", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "mypage-edit";
        }

        // 원문 비밀번호를 UserDto.password에 넣어줌 → 서비스에서 암호화
        form.setPassword(newPassword);
    } else {
        // 비밀번호 변경 안 함
        form.setPassword(null);
    }

    // 🔍 비밀번호 세팅한 뒤에도 한 번 더 찍어보자
    System.out.println(
            "[MypageController.beforeUpdate] id=" + form.getId()
                    + ", nickname=" + form.getNickname()
                    + ", email=" + form.getEmail()
                    + ", phone=" + form.getPhone()
                    + ", rawPassword=" + form.getPassword()
    );

    try {
        // 1) 회원 정보 수정 (닉네임, 이메일, 전화번호, (선택) 비밀번호)
        authService.updateProfile(form);

        // 2) 닉네임이 변경되면 관련 테이블도 업데이트
        if (!oldNickname.equals(newNickname)) {
            chatRoomService.updateMemberNickname(oldNickname, newNickname);
            inquiryService.updateWriterNickname(oldNickname, newNickname);
        }

        // 3) 세션 최신화
        UserDto updated = authService.findById(loginUser.getId());
        session.setAttribute("loginUser", updated);

        return "redirect:/mypage";

    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("auth", form);
        model.addAttribute("errorMsg", "수정 중 오류가 발생했습니다.");
        return "mypage-edit";
    }
}


}
