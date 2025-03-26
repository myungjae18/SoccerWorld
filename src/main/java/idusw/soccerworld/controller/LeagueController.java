package idusw.soccerworld.controller;

import idusw.soccerworld.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LeagueController {
    private final TeamService teamService;

    public LeagueController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/league/statistics")
    public String goStatistics(Model model) {
        model.addAttribute("standingsList", teamService.getAllStandings());

        return "/league/statistics";
    }
}
