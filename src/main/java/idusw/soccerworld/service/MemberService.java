package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.Member;
import idusw.soccerworld.domain.dto.MemberDetails;
import idusw.soccerworld.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserDetailsService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public int regist() {
        //memberRepository.insert();
        return 0;
    }

    //security를 통한 로그인 처리
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.loginCheck(id);

        if (member == null) {
            throw new UsernameNotFoundException("Member not found with id: " + id);
        }

        return new MemberDetails(member);
    }

    //회원가입 처리
    public void insertMember(Member member) {
        memberRepository.insertOne(member);
    }

    //아이디 중복 확인
    public String selectById(String id) {
        return memberRepository.selectOneById(id);
    }

    //닉네임 중복 확인
    public String selectByNickname(String nickname) {
        return memberRepository.selectOneByNickname(nickname);
    }
}
