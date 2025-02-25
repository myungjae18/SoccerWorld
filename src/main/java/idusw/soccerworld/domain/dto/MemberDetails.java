package idusw.soccerworld.domain.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

//security를 위한 인증용 유저 정보 객체
public class MemberDetails implements UserDetails {
    private final String username;
    private final String password;
    private final String role;

    //member에서 필요한 데이터만 가져옴
    public MemberDetails(Member member) {
        this.username = member.getId();
        this.password = member.getPassword();
        this.role = member.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
