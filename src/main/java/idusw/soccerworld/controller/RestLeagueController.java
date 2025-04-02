package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.StandingsDto;
import idusw.soccerworld.domain.dto.StatisticsDto;
import idusw.soccerworld.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class RestLeagueController {
    private TeamService teamService;

    @GetMapping("/standings/{season}/{league}")
    public List<StandingsDto> getStandings(@PathVariable String season, @PathVariable String league) {
        StandingsDto standingsDto = StandingsDto.builder()
                .league(league).season(season).build();

        return teamService.getStandingsByLeagueSeason(standingsDto);
    }

    @GetMapping("/statistics/{season}/{league}")
    public List<StatisticsDto> getStatistics(@PathVariable String season, @PathVariable String league) {
        StatisticsDto statisticsDto = StatisticsDto.builder()
                .league(league).season(season).build();

        return teamService.getStatisticsByLeagueSeason(statisticsDto);
    }
}
