package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.Member;
import idusw.soccerworld.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //로그인 페이지
    @GetMapping("/member/login")
    public String goLogin(Model model) {
        //post로 보낼 비어있는 dto 객체 전송
        model.addAttribute("member", new Member());

        return "/member/login";
    }

    //회원가입 요청
    @PostMapping("/member/register")
    public String register(@ModelAttribute Member member) {
        memberService.insertMember(member);

        return "/main/index";
    }
}
