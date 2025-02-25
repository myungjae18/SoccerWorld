package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.Team;
import idusw.soccerworld.service.TeamService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
public class RestTeamController {
    private RestClient restClient;
    private final TeamService teamService;

    public RestTeamController(RestClient restClient, TeamService teamService) {
        this.restClient = restClient;
        this.teamService = teamService;
    }

    @GetMapping("/teams/league/{leagueName}")
    public List<Team> getTeams(@PathVariable String leagueName, Model model) {
        //model 객체 내에 있는 전체 팀 리스트 가져오기
        List<Team> totalList = (List<Team>) model.getAttribute("fragmentData");
        //파라미터로 넘어온 리그와 일치하는 팀만 남기기
        totalList.removeIf(team -> !team.getLeague().equals(leagueName));

        return totalList;
    }
}
