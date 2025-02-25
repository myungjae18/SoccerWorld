package idusw.soccerworld.domain.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    private long teamId;
    private String name;
    private String league;
    private String logo;
    private String headCoach;
    private String stadium;
    private String location;
}
