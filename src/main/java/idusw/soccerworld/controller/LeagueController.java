package idusw.soccerworld.controller;

import idusw.soccerworld.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LeagueController {
    @GetMapping("/league/statistics")
    public String goStatistics(Model model) {
        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", model.getAttribute("fragmentData"));

        return "/league/statistics";
    }
}
