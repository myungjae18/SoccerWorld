package idusw.soccerworld.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDto {
    private String statisticsId;
    private long playerId;
    private String playerName;
    private TeamDto teamDto;
    private Integer playedMatches;
    private Integer goals;
    private Integer assists;
    private Integer penalties;
    private String season;
    private String league;
}