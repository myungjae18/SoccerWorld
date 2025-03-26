package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.GameDto;

import idusw.soccerworld.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;


@Controller
public class MainController {
    private final TeamService teamService;
    private final GameService gameService;
    private final PostService postService;
    private final NewsService newsService;

    //생성자 주입
    public MainController(
            TeamService teamService, GameService gameService, PostService postService, NewsService newsService) {
        this.teamService = teamService;
        this.gameService = gameService;
        this.postService = postService;
        this.newsService = newsService;
    }

    //메인 페이지 이동
    @GetMapping("/main/index")
    public String goIndex(Model model) {
        //현재 시각을 기준으로 그 주의 모든 경기 가져오기
        model.addAttribute("gameMap", gameService.getGamesByWeekRandom());

        //모든 리그의 순위 정보 가져오기
        model.addAttribute("standingsMap", teamService.getAllCurrentStandings());

        //뉴스 헤드라인 가져오기
        model.addAttribute("headLines", newsService.getHeadLines());

        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", model.getAttribute("fragmentData"));

        //카테고리 별 인기 게시물 가져오기


        return "/main/index";
    }

    //에러 페이지 처리
    @GetMapping("/error")
    public String go404() {
        return "/error/404";
    }
}