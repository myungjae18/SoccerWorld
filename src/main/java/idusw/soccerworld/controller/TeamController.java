package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.TeamDto;
import idusw.soccerworld.service.GameService;
import idusw.soccerworld.service.GameApiService;
import idusw.soccerworld.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class TeamController {
    final TeamService teamService;
    final GameService gameService;
    final GameApiService gameApiService;
    public TeamController(TeamService teamService,
                          GameService gameService,
                          GameApiService gameApiService){
        this.teamService = teamService;
        this.gameService = gameService;
        this.gameApiService = gameApiService;
    }

    @GetMapping("/admin/football-data")
    public String getPremierLeague(@RequestParam(required = false, value = "league")String league ,Model model){
        int leagueNum = 0;
        String leagueName = null;
        if(league.equals("PL")) {
            leagueNum = 2021;
            leagueName = "프리미어리그";
        } else if (league.equals("PD")) {
            leagueNum = 2014;
            leagueName = "라리가";
        } else if (league.equals("BL1")) {
            leagueNum = 2002;
            leagueName = "분데스리가";
        } else if (league.equals("SA")) {
            leagueNum = 2019;
            leagueName = "세리에A";
        }
        model.addAttribute("leagueNum", leagueNum);
        model.addAttribute("leagueName", leagueName);
        return "/admin/football-data";
    }

    //팀 정보 페이지
    @GetMapping("/team/info")
    public String goInfo(@RequestParam(value = "team-id") String teamId, Model model) {
        List<TeamDto> teamList = (List<TeamDto>) model.getAttribute("fragmentData");

        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", teamList);
        //teamList에서 teamId가 같은 teamDto 추출하여 전송
        model.addAttribute("info", teamService.getByPk(teamId));

        return "/team/info";
    }


    @GetMapping("/admin/schedule")
    @ResponseBody
    public Object getFixture(@RequestParam(required = false, value = "selectedDate")String paramDate,
                             @RequestParam(required = false, value = "leagueNum")int leagueNum,
                             @RequestParam(required = false, value = "season")String season) {
        ResponseEntity<Map> response;
        if("0".equals(paramDate)) {
            if(season == null || season.isEmpty() || "undefined".equals(season)) {
                response =  gameApiService.getGameApiByCurrentSeason(leagueNum);
            } else {
                response = gameApiService.getGameApiByPastSeason(leagueNum, season);
            }

        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(paramDate,formatter);
            LocalDate previousDate = date.minusDays(1);
            String fromDate = previousDate.format(formatter);
            String toDate = paramDate;
            response = gameApiService.getGameApiByLeagueAndDate(leagueNum,fromDate,toDate);
        }
        return response;

    }

    @GetMapping("/admin/teamInfo")
    @ResponseBody
    public Object getTeamInfo(@RequestParam(required = false,value="leagueNum")int leagueNum,
                              @RequestParam(required = false, value = "season")String season){
        ResponseEntity<Map> response;
        if(season == null || season.isEmpty() || "undefined".equals(season)) {
            response = teamService.getTeamInfo(leagueNum);
        } else {
            response = teamService.getTeamPastInfo(leagueNum,season);
        }
        return response;
    }


    @GetMapping("/admin/standingInfo")
    @ResponseBody
    public Object getStandingInfo(@RequestParam(required = false,value="leagueNum")int leagueNum,
                                  @RequestParam(required = false, value = "season")String season){
        ResponseEntity<Map> response;
        if(season == null || season.isEmpty() || "undefined".equals(season)) {
            response = teamService.getStandingInfo(leagueNum);
        } else {
            response = teamService.getStandingPastInfo(leagueNum,season);
        }
        return response;
    }

    @GetMapping("/admin/statisticsInfo")
    @ResponseBody
    public  Object getStatisticsInfo(@RequestParam(required = false,value="leagueNum")int leagueNum,
                                     @RequestParam(required = false, value = "season")String season){
        ResponseEntity<Map> response;
        if(season == null || season.isEmpty() || "undefined".equals(season)) {
            response = teamService.getStatisticsInfo(leagueNum);
        } else {
            response = teamService.getStatisticsPastInfo(leagueNum,season);
        }
        return response;
    }

    @PostMapping("/admin/insertData")
    @ResponseBody
    public ResponseEntity insertData(@RequestBody Map<String, Object> jsonData){
        int result = 0;
        if(jsonData.get("type").equals("games")) {
            result = gameService.insertGames((Map<String, Object>) jsonData.get("data"));
        } else if(jsonData.get("type").equals("teams")){
            result = teamService.insertTeamInfo((Map<String, Object>) jsonData.get("data"));
        } else if(jsonData.get("type").equals("standings")){
            result = teamService.insertStandingInfo((Map<String, Object>) jsonData.get("data"));
        } else if(jsonData.get("type").equals("statistics")){
            result = teamService.insertStatisticsInfo((Map<String, Object>) jsonData.get("data"));
        } else if(jsonData.get("type").equals("player")){
            result = teamService.insertPlayerInfo((Map<String, Object>) jsonData.get("data"));
        }


        if(result > 0) {
            return new ResponseEntity<>("성공적으로 등록되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("게임 등록 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}