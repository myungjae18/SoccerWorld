package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.GameDto;
import idusw.soccerworld.domain.dto.PredictionDto;
import idusw.soccerworld.service.GameService;
import idusw.soccerworld.service.PredictionService;
import idusw.soccerworld.service.GameApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class FixtureController {
    final GameService gameService;
    final PredictionService predictionService;
    final GameApiService gameApiService;

    public FixtureController(GameService gameService,
                             PredictionService predictionService,
                             GameApiService gameApiService) {
        this.gameService = gameService;
        this.predictionService = predictionService;
        this.gameApiService = gameApiService;
    }


    @GetMapping("fixture/schedule")
    public String goPrediction(@RequestParam(required = false, value = "league") String league,
                               @RequestParam(required = false, value = "selectedDate")String date,
                               @RequestParam(required = false, value = "round") Integer round,
                               Model model) {
        List<GameDto> gameDtoList;
        LocalDateTime dateTime;

        if (date != null) {
            dateTime = LocalDateTime.parse(date + "T00:00:00");
        } else {
            dateTime = LocalDateTime.now();
//            dateTime = LocalDateTime.parse("2025-04-03T00:00:01");
        }

        GameDto gameDto = GameDto.builder()
                .gameId(0)
                .dateTime(dateTime)
                .league(league)
                .round(round)
                .build();

        if(date != null){ //날짜 선택 시
            gameDtoList = gameService.getGamesByDate(gameDto);
        } else {
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            model.addAttribute("isRound", "y");
            if (round != null) { //라운드 선택시
                gameDtoList = gameService.getGamMoreByRound(gameDto);
            } else { //아무것도 선택하지않은 디폴트값 (그 주 경기 반환)
                gameDtoList = gameService.getGameByWeek(gameDto);

                if(gameDtoList.size() == 0) { //만약 a매치기간 이거나 일정 변동하여 1주동안 경기가 없을때 2주치 경기 불러옴
                    gameDtoList = gameService.getGameByTwoWeek(gameDto);
                    round = gameDtoList.get(0).getRound();
                } else {
                    round = gameDtoList.get(0).getRound();
                }

            }
        }

        if (!gameDtoList.isEmpty()){
            List<PredictionDto> predictionDtoList = predictionService.getPredictions(gameDtoList); //예측게임에 해당하는 예측테이블 정보들 불러옴
            Map<Long, Map<String, String>> predictionPercentages = predictionService.getPredictionPercentages(predictionDtoList);   //예측게임의 gameId 기준으로 게임의 예측값들을 100분율 퍼센트 예측률로 구하기
            model.addAttribute("predictions",predictionPercentages);
        }

        model.addAttribute("teamList", model.getAttribute("fragmentData"));
        model.addAttribute("Games",gameDtoList);
        model.addAttribute("today",date);
        model.addAttribute("currentRound", round);

        if(league != null) {
            model.addAttribute("leagueName",league);
            return "fixture/schedule";
        } else {
            return "/error/404";
        }

    }


    @GetMapping("/posts")
    public String moreGames(@RequestParam(required = false,value = "lastRound") Integer lastRound,
                            @RequestParam(required = false,value = "leagueParam") String league,Model model){
        GameDto gameDto = GameDto.builder()
                .gameId(0)
                .round(lastRound)
                .league(league)
                .build();
        List<GameDto> gameDtoList = gameService.getGamMoreByRound(gameDto);
        model.addAttribute("isRound","y");

        System.out.println("라운드:"+lastRound);
        System.out.println("포스트:"+gameDtoList);
        model.addAttribute("Games", gameDtoList);
        if (!gameDtoList.isEmpty()){
            List<PredictionDto> predictionDtoList = predictionService.getPredictions(gameDtoList); //예측게임에 해당하는 예측테이블 정보들 불러옴
            Map<Long, Map<String, String>> predictionPercentages = predictionService.getPredictionPercentages(predictionDtoList);   //예측게임의 gameId 기준으로 게임의 예측값들을 100분율 퍼센트 예측률로 구하기
            model.addAttribute("predictions",predictionPercentages);
        }
        return "fixture/schedule :: matchList";
    }
}