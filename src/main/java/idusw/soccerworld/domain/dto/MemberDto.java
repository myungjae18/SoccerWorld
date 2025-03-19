package idusw.soccerworld.domain.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class MemberDto {
    private long memberId;
    private String id;
    private String password;
    private String name;
    private String phone;
    private int gender;
    private Date birthday;
    private String nickname;
    private int point;
    private String role;
    private TeamDto teamDto;
}
