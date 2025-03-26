package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.MemberDetails;
import idusw.soccerworld.domain.dto.MemberDto;
import idusw.soccerworld.domain.dto.TeamDto;
import idusw.soccerworld.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //로그인 페이지
    @GetMapping("/member/login")
    public String goLogin(@RequestParam(value = "error", required = false) String error,
                          Model model) {
        //로그인 요청 실패 시 메시지 전달
        if (error != null && error.equals("login")) {
            model.addAttribute("message", "비밀번호 또는 아이디를 확인해주세요");
        }

        return "/member/login";
    }

    //회원가입 요청
    @PostMapping("/member")
    public String register(@ModelAttribute MemberDto memberDto, RedirectAttributes redirectAttributes,
                           @RequestParam String teamId) {
        //view에서 가져온 가입 정보 전달
        memberDto.setTeamDto(TeamDto.builder()
                .teamId(Long.valueOf(teamId))
                .build());
        memberDto.setPoint(500);

        int result = memberService.insertMember(memberDto);

        //view에 회원 가입 성공 여부에 따른 메세지 전달
        if (result == 1) redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
        else redirectAttributes.addFlashAttribute("message", "서버 오류가 발생했습니다. 다시 시도해 주세요");
        return "redirect:/main/index";
    }

    //회원 정보 페이지 요청
    @GetMapping("/member/info")
    public String goInfo(@RequestParam String type, Model model) {
        model.addAttribute("type", type);

        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", model.getAttribute("fragmentData"));

        return "/member/info";
    }

    //닉네임 중복 확인
    @GetMapping("/member/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String nickname) {
        boolean isAvailable = false;
        Map<String, Boolean> response = new HashMap<>();
        String result = memberService.selectByNickname(nickname);
        if (result == null) {
            isAvailable = true;
        }
        response.put("available", isAvailable);

        return ResponseEntity.ok(response);
    }

    //회원 정보 수청 요청
    @PutMapping("/member/{memberId}")
    public String edit(@ModelAttribute MemberDto memberDto, RedirectAttributes redirectAttributes) {
        int result = memberService.updateMember(memberDto);

        //view에 정보 수정 성공 여부에 따른 메세지 전달
        if (result == 1) {
            MemberDto updatedDto = memberService.getMemberByMemberId(memberDto.getMemberId());
            //갱신된 유저 정보를 가져와 token에 주입
            UserDetails userDetails = new MemberDetails(updatedDto);
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            //토큰을 통해 새로운 인증 객체 설정
            SecurityContextHolder.getContext().setAuthentication(token);

            redirectAttributes.addFlashAttribute("message", "회원 정보 수정이 완료되었습니다.");
        }
        else redirectAttributes.addFlashAttribute("message", "서버 오류가 발생했습니다. 다시 시도해 주세요");

        return "redirect:/member/info?type=info";
    }
}
