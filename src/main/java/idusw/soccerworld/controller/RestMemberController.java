package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.MemberDetails;
import idusw.soccerworld.domain.dto.MemberDto;
import idusw.soccerworld.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestMemberController {
    private MemberService memberService;

    public RestMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //아이디 검색 후 결과 반환
    @GetMapping("/member/id/{id}")
    public String idCheck(@PathVariable String id) {
        return memberService.selectById(id);
    }

    //닉네임 검색 후 결과 반환
    @GetMapping("/member/nickname/{nickname}")
    public String nicknameCheck(@PathVariable String nickname) {
        return memberService.selectByNickname(nickname);
    }


    //회원 정보 한 개 수청 요청(변경하는 필드가 달라도 같은 url 사용)
    @PatchMapping("/member/{memberId}")
    public ResponseEntity editOne(
            @PathVariable Long memberId, @RequestBody MemberDto memberDto) {
        memberDto.setMemberId(memberId);
        int result = memberService.updateOne(memberDto);//업데이트 후 결과값 반환

        if (result < 1) return ResponseEntity.badRequest().body("cannot update nickname");//실패 시
        else {
            MemberDto updatedDto = memberService.getMemberByMemberId(memberDto.getMemberId());
            //갱신된 유저 정보를 가져와 token에 주입
            UserDetails userDetails = new MemberDetails(updatedDto);
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            //토큰을 통해 새로운 인증 객체 설정
            SecurityContextHolder.getContext().setAuthentication(token);

            return ResponseEntity.ok("update successfully");//성공 시
        }
    }
}
