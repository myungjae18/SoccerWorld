package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.MemberDto;
import idusw.soccerworld.domain.dto.MemberDetails;
import idusw.soccerworld.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    //security를 통한 로그인 처리
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        MemberDto memberDto = memberRepository.loginCheck(id);

        if (memberDto == null) {
            throw new UsernameNotFoundException("MemberDto not found with id: " + id);
        }

        return new MemberDetails(memberDto);
    }

    //회원가입 처리
    public int insertMember(MemberDto memberDto) {
        String encodedPassword = passwordEncoder.encode(memberDto.getPassword());
        memberDto.setPassword(encodedPassword);
        return memberRepository.insertOne(memberDto);
    }

    //아이디 중복 확인
    public String selectById(String id) {
        return memberRepository.selectOneById(id);
    }

    //닉네임 중복 확인
    public String selectByNickname(String nickname) {
        return memberRepository.selectOneByNickname(nickname);
    }

    public MemberDto getMemberByMemberId (long memberId) {
        MemberDto memberDto = memberRepository.selectByMemberId(memberId);
        return memberDto;
    }

    //회원 정보 수정
    public int updateMember(MemberDto memberDto) {
        return memberRepository.update(memberDto);
    }

    //정보 한 개 수정
    public int updateOne(MemberDto memberDto) {
        //수정하려는 데이터의 종류에 따른 메서드 호출
        if(memberDto.getPassword() != null) {
            memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
            return memberRepository.updatePassword(memberDto);
        } else if(memberDto.getNickname() != null) {
            return memberRepository.updateNickname(memberDto);
        } else {
            return memberRepository.updateTeam(memberDto);
        }
    }

    public int updateMemberPoint(Long memberId, int whether){
        if(whether == 0) {
            return memberRepository.updatePointGetByMemberId(memberId);
        } else {
            return memberRepository.updatePointLoseByMemberId(memberId);
        }
    }
}
