package idusw.soccerworld.controller;

import idusw.soccerworld.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final LeagueService leagueService;
    private final GameService gameService;
    private final CategoryService categoryService;
    private final NewsService newsService;

    //생성자 주입
    public MainController(
            LeagueService leagueService, GameService gameService, CategoryService categoryService, NewsService newsService) {
        this.leagueService = leagueService;
        this.gameService = gameService;
        this.categoryService = categoryService;
        this.newsService = newsService;
    }

    //메인 페이지 이동
    @GetMapping("/main/index")
    public String goIndex(Model model) {
        //4대 리그의 순위 정보 가져오기
        model.addAttribute("pLStand",leagueService.getStandingsByApi("PL"));
        model.addAttribute("laLigaStand",leagueService.getStandingsByApi("PD"));
        model.addAttribute("serieAStand",leagueService.getStandingsByApi("SA"));
        model.addAttribute("bundesStand",leagueService.getStandingsByApi("BL1"));

        //뉴스 헤드라인 가져오기
        model.addAttribute("headLines", newsService.getHeadLines());

        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", model.getAttribute("fragmentData"));

        return "/main/index";
    }

    //에러 페이지 처리
    @GetMapping("/error")
    public String go404() {
        return "/error/404";
    }
}