package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.MemberDto;
import idusw.soccerworld.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        model.addAttribute("memberDto", new MemberDto());

        return "/member/login";
    }

    //회원가입 요청
    @PostMapping("/member/register")
    public String register(@ModelAttribute MemberDto memberDto) {
        memberService.insertMember(memberDto);

        return "/main/index";
    }
}
