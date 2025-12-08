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

import java.util.List;

@Controller
public class MypageController {

    private final ChatRoomService chatRoomService;
    private final InquiryService inquiryService;
    private final AuthService authService;   // 🔹 final 로 만들고

    // 🔹 생성자에서 주입받기
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
        model.addAttribute("auth", auth);

        return "mypage-edit";
    }

    /** 수정 처리 */
    @PostMapping("/mypage/edit")
    public String editSubmit(@ModelAttribute UserDto form,
                             HttpSession session,
                             Model model) {

        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 로그인한 본인만 업데이트
        form.setId(loginUser.getId());

        try {
            // 🔹 static 호출 말고, 주입받은 서비스 인스턴스 사용
            authService.updateProfile(form);

            // 세션도 최신 값으로 갱신
            UserDto updated = authService.findById(loginUser.getId());
            session.setAttribute("loginUser", updated);

            model.addAttribute("auth", updated);
            model.addAttribute("successMsg", "정보가 수정되었습니다!");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("auth", form);
            model.addAttribute("errorMsg", "수정 중 오류가 발생했습니다.");
        }

        return "redirect:/mypage";

    }
}
