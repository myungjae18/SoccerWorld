package idusw.soccerworld.domain.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
//security를 위한 인증용 유저 정보 객체
public class MemberDetails implements UserDetails {
    private String username;
    private String password;
    private String role;
    private Long memberId;
    private String name;
    private String phone;
    private int gender;
    private Date birthday;
    private String nickname;
    private TeamDto teamDto;
    private int point;
    private Collection<? extends GrantedAuthority> authorities;

    //member에서 데이터 가져옴
    public MemberDetails(MemberDto memberDto) {
        this.username = memberDto.getId();
        this.password = memberDto.getPassword();
        this.role = memberDto.getRole();
        this.memberId = memberDto.getMemberId();
        this.name = memberDto.getName();
        this.phone = memberDto.getPhone();
        this.gender = memberDto.getGender();
        this.birthday = memberDto.getBirthday();
        this.nickname = memberDto.getNickname();
        this.teamDto = memberDto.getTeamDto();
        this.point = memberDto.getPoint();
    }

    //권한(role)을 반환하는 메서드. 보통 ROLE_을 접두사로 사용한다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role));  // role이 "Client"이면 "ROLE_Client"가 된다.

        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
