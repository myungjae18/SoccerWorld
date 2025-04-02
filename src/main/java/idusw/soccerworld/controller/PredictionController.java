package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.GameDto;
import idusw.soccerworld.domain.dto.MemberDto;
import idusw.soccerworld.domain.dto.PredictionDto;
import idusw.soccerworld.service.GameService;
import idusw.soccerworld.service.MemberService;
import idusw.soccerworld.service.PredictionService;
import idusw.soccerworld.service.GameApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class PredictionController {
    final MemberService memberService;
    final GameService gameService;
    final PredictionService predictionService;
    final GameApiService gameApiService;

//    LocalDate today = LocalDate.now();
//
//    // 이번 주의 시작일 계산 (월요일)
//    LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
//
//    // 이번 주의 끝일 계산 (일요일)
//    LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
//
//    // 날짜 포맷 지정 (예: yyyy-MM-dd)
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//    String startDayFormat = startOfWeek.format(formatter);
//    String endDayFormat = endOfWeek.format(formatter);

    public PredictionController(MemberService memberService,
                                GameService gameService,
                                PredictionService predictionService,
                                GameApiService gameApiService){
        this.memberService = memberService;
        this.gameService = gameService;
        this.predictionService = predictionService;
        this.gameApiService = gameApiService;
    }
    @GetMapping("/prediction")
    public String goPrediction(Model model) {
        LocalDateTime dateTime = LocalDateTime.now();
        List<GameDto> plDtoList = null;
        List<GameDto> pdDtoList = null;
        List<GameDto> bl1DtoList = null;
        List<GameDto> saDtoList = null;

        GameDto plGame = GameDto.builder()
                .gameId(0)
                .league("PL")
                .dateTime(dateTime).build();

        GameDto pdGame = GameDto.builder()
                .gameId(0)
                .league("PD")
                .dateTime(dateTime).build();

        GameDto bl1Game = GameDto.builder()
                .gameId(0)
                .league("BL1")
                .dateTime(dateTime).build();

        GameDto saGame = GameDto.builder()
                .gameId(0)
                .league("SA")
                .dateTime(dateTime).build();

        plDtoList = gameService.getGameByWeek(plGame);
        pdDtoList = gameService.getGameByWeek(pdGame);
        bl1DtoList = gameService.getGameByWeek(bl1Game);
        saDtoList = gameService.getGameByWeek(saGame);


        if (plDtoList != null && !plDtoList.isEmpty()){
            List<PredictionDto> predictionDtoList = predictionService.getPredictions(plDtoList); //예측게임에 해당하는 예측테이블 정보들 불러옴
            Map<Long, Map<String, String>> predictionPercentages = predictionService.getPredictionPercentages(predictionDtoList);   //예측게임의 gameId 기준으로 게임의 예측값들을 100분율 퍼센트 예측률로 구하기
            model.addAttribute("plPredictions",predictionPercentages);
        }

        if (pdDtoList != null && !pdDtoList.isEmpty()){
            List<PredictionDto> predictionDtoList = predictionService.getPredictions(pdDtoList); //예측게임에 해당하는 예측테이블 정보들 불러옴
            Map<Long, Map<String, String>> predictionPercentages = predictionService.getPredictionPercentages(predictionDtoList);   //예측게임의 gameId 기준으로 게임의 예측값들을 100분율 퍼센트 예측률로 구하기
            model.addAttribute("pdPredictions",predictionPercentages);
        }

        if (bl1DtoList != null && !bl1DtoList.isEmpty()){
            List<PredictionDto> predictionDtoList = predictionService.getPredictions(bl1DtoList); //예측게임에 해당하는 예측테이블 정보들 불러옴
            Map<Long, Map<String, String>> predictionPercentages = predictionService.getPredictionPercentages(predictionDtoList);   //예측게임의 gameId 기준으로 게임의 예측값들을 100분율 퍼센트 예측률로 구하기
            model.addAttribute("bl1Predictions",predictionPercentages);
        }

        if (saDtoList != null && !saDtoList.isEmpty()){
            List<PredictionDto> predictionDtoList = predictionService.getPredictions(bl1DtoList); //예측게임에 해당하는 예측테이블 정보들 불러옴
            Map<Long, Map<String, String>> predictionPercentages = predictionService.getPredictionPercentages(predictionDtoList);   //예측게임의 gameId 기준으로 게임의 예측값들을 100분율 퍼센트 예측률로 구하기
            model.addAttribute("saPredictions",predictionPercentages);
        }

        model.addAttribute("teamList", model.getAttribute("fragmentData"));
        model.addAttribute("plGames",plDtoList);
        model.addAttribute("pdGames",pdDtoList);
        model.addAttribute("bl1Games",bl1DtoList);
        model.addAttribute("saGames",saDtoList);

        return "fixture/prediction";
    }



    @PostMapping("/prediction")
    @ResponseBody
    public ResponseEntity inputPrediction(@RequestBody Map<String, Object> predictionData) {
        int memberId = (int) predictionData.get("memberId");
        int gameId = (int) predictionData.get("gameId");
        int result = (int) predictionData.get("result");
        String gameTimeParam = (String) predictionData.get("gameTime");
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime gameTimeZone = ZonedDateTime.parse(gameTimeParam);
        LocalDateTime gameTime = gameTimeZone.toLocalDateTime();
        System.out.println("클릭시간:"+now);
        System.out.println("경기시간:"+gameTime);
        if (now.isBefore(gameTime)) {
            PredictionDto predictionDto = new PredictionDto();
            MemberDto memberDto = memberService.getMemberByMemberId(memberId); //예측 테이블 중복값을 확인하기위한 MemberDto(DB = memeber_id) 확인
            GameDto gameDto = gameService.getGameByGameId(gameId); //예측 테이블 중복값을 확인하기 위한 GameDto(DB = game_id) 확인
            System.out.println("멤버 정보" + memberDto);
            System.out.println("게임 정보" + gameDto);
            predictionDto.setResult(result);
            predictionDto.setMemberDto(memberDto);
            predictionDto.setGameDto(gameDto);

            if (predictionService.checkPrediction(predictionDto) == "가능") { //예측 테이블 중복확인 서비스
                predictionService.insertPrediction(predictionDto);
                return new ResponseEntity<>("성공", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("재투표", HttpStatus.BAD_REQUEST);
            }

        } else if (now.isAfter(gameTime)) {
            return new ResponseEntity<>("투표기간제한", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("투표기간제한", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/login")
    public String login(){
        return "/main/login";
    }


}