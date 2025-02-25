package idusw.soccerworld.controller;

import idusw.soccerworld.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {
    private TeamService teamService;

    public AdminController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/api-connect/data")
    public String goApiConnect() {
        return "/admin/api-connect/data";
    }

//    //4대 리그 내 팀 정보를 모두 DB에 저장 후 view return
//    @GetMapping("/get-teams")
//    public String getTeams() {
//        //4대 리그의 정보 DB에 저장
//        teamService.getTeamsByApi("PL", "Premier League");
//        teamService.getTeamsByApi("PD", "La Liga");
//        teamService.getTeamsByApi("SA", "Serie A");
//        teamService.getTeamsByApi("BL1", "Bundesliga");
//
//        return "/admin/api-connect/data";
//    }
}
