package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.GameDto;
import idusw.soccerworld.domain.dto.PredictionDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchedulerService {
    GameService gameService;
    PredictionService predictionService;
    MemberService memberService;
    TeamService teamService;
    GameApiService gameApiService;

    public SchedulerService(GameService gameService,
                            PredictionService predictionService,
                            MemberService memberService,
                            TeamService teamService,
                            GameApiService gameApiService){
        this.gameService = gameService;
        this.predictionService = predictionService;
        this.memberService = memberService;
        this.teamService = teamService;
        this.gameApiService = gameApiService;
    }

    @Scheduled(cron = "0 39 15 * * *", zone = "Asia/Seoul")
    public void refreshPoint(){
        LocalDateTime nowTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime yesterdayTime = nowTime.toLocalDate().minusDays(1).atStartOfDay();
        Map<String,Object> nowAndYester = new HashMap<>();
        nowAndYester.put("now",nowTime);
        nowAndYester.put("yesterday",yesterdayTime);

        List<GameDto> gameDtoList = gameService.getFinishedGameByToday(nowAndYester);//24시간마다 오늘날짜 기준으로 전날과 오늘중에 끝난경기 불러오기

        if (!gameDtoList.isEmpty() && gameDtoList != null ) {
            List<PredictionDto> successPredictionDtoList = predictionService.getPredictionsByResult(gameDtoList,0); //불러온 경기와 그경기결과와 맞는 예측을한 테이블과 참조된 유저 가져오기
            List<PredictionDto> failedPredictionDtoList = predictionService.getPredictionsByResult(gameDtoList,1);  //성공 0 , 실패 1

            for (PredictionDto predictionDto : successPredictionDtoList) { //예측 성공
                if (predictionDto.getStatus() == 0) { // 예측테이블의 상태가 0일때 (유저 포인트 처리가 아직 안된 상태)
                    predictionDto.setStatus((byte) 1);
                    predictionService.updateStatus(predictionDto); //해당 예측테이블 상태 변경
                    memberService.updateMemberPoint(predictionDto.getMemberDto().getMemberId(),0); //해당 유저 포인트 주입
                }
            }

            for (PredictionDto predictionDto : failedPredictionDtoList){ //예측 실패
                if(predictionDto.getStatus() == 0) {
                    predictionDto.setStatus((byte) 2);
                    predictionService.updateStatus(predictionDto);
                    memberService.updateMemberPoint(predictionDto.getMemberDto().getMemberId(),1);
                }
            }


        }
    }

    @Scheduled(cron = "0 16 15 * * *",zone = "Asia/Seoul")
    public void refreshPointStandingAndStatistics(){
        List<Integer> leagueIds = List.of(2021, 2014, 2002, 2019);

        // Standings 업데이트
        leagueIds.forEach(id -> {
            Map<String, Object> standings = teamService.getStandingInfo(id).getBody();
            teamService.insertStandingInfo(standings);
        });

        // Statistics 업데이트
        leagueIds.forEach(id -> {
            Map<String, Object> statistics = teamService.getStatisticsInfo(id).getBody();
            teamService.insertStatisticsInfo(statistics);
        });

    }

    //초 분 시 날짜
    @Scheduled(cron = "0 45 15 * * *",zone = "Asia/Seoul")
    public void refreshGames(){
        List<Integer> leagueIds = List.of(2021, 2014, 2002, 2019);
        leagueIds.forEach(id -> {
            Map<String, Object> games = gameApiService.getGameApiByCurrentSeason(id).getBody();
            gameService.insertGames(games);
        });
    }
}