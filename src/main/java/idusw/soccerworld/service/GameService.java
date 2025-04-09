package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.GameDto;
import idusw.soccerworld.domain.dto.TeamDto;
import idusw.soccerworld.repository.GameRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {
    final GameRepository gameRepository;
    private RestClient restClient;
    private GameApiService scheduleApiService;

    public static LocalDateTime convertUtcToKst(String utcDateTimeStr) {
        ZonedDateTime utcDateTime = ZonedDateTime.parse(utcDateTimeStr);
        ZonedDateTime kstDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return kstDateTime.toLocalDateTime();
    }

    public GameService(GameRepository gameRepository,
                       @Qualifier("restClient") RestClient restClient,
                       GameApiService scheduleApiService) {
        this.gameRepository = gameRepository;
        this.restClient = restClient;
        this.scheduleApiService = scheduleApiService;
    }

    public List<GameDto> getGamesByDate(GameDto gameDto) {
        List<GameDto> gameDtoList = gameRepository.selectByDate(gameDto);
        return gameDtoList;
    }

    public GameDto getGameByGameId(int gameId) { //
        GameDto gameDto = gameRepository.selectByGameId(gameId);
        return gameDto;
    }


    public int insertGames(Map<String, Object> gamesData) {

        Map competition = (Map) gamesData.get("competition");
        List<Map> gameDataList = (List<Map>) gamesData.get("matches");
        List<GameDto> gameDtoList = new ArrayList<>();

        int result;

        for (Map gameData : gameDataList) {

            Map gameScore = (Map) gameData.get("score");
            Map gameGoals = (Map) gameScore.get("fullTime");
            Map gameTeamHome = (Map) gameData.get("homeTeam");
            Map gameTeamAway = (Map) gameData.get("awayTeam");

            TeamDto teamHomeDto = TeamDto.builder()
                    .teamId((int) gameTeamHome.get("id"))
                    .build();
            TeamDto teamAwayDto = TeamDto.builder()
                    .teamId((int) gameTeamAway.get("id"))
                    .build();

            int homeScore = 0;
            int awayScore = 0;
            int gameResult = 3;

            if (gameScore.get("winner") != null) {
                homeScore = (int) gameGoals.get("home");
                awayScore = (int) gameGoals.get("away");

                if ("HOME_TEAM".equals(gameScore.get("winner").toString())) {
                    gameResult = 1;
                } else if ("DRAW".equals(gameScore.get("winner").toString())) {
                    gameResult = 0;
                } else if ("AWAY_TEAM".equals(gameScore.get("winner").toString())) {
                    gameResult = 2;
                }
            }

            GameDto gameDto = GameDto.builder()
                    .gameId((int) gameData.get("id"))
                    .homeTeamDto(teamHomeDto)
                    .awayTeamDto(teamAwayDto)
                    .dateTime(convertUtcToKst(gameData.get("utcDate").toString()))
                    .round((Integer) gameData.get("matchday"))
                    .league(competition.get("code").toString())
                    .status(gameData.get("status").toString())
                    .homeScore(homeScore)
                    .awayScore(awayScore)
                    .result(gameResult)
                    .build();

            gameDtoList.add(gameDto);
        }
        result = gameRepository.insertGames(gameDtoList);
        return result;
    } //외부에서 데이터를 호출하고 post요청으로 데이터를 보내 백엔드에서 저장할 시


    public List<GameDto> getGamMoreByRound(GameDto gameDto) { // 게임 더보기 (라운드 순)
        if (gameDto.getRound() == null) {
            gameDto.setRound(scheduleApiService.getGameApiCurrentMatchDay());
            return gameRepository.selectMore(gameDto);
        } else {
            return gameRepository.selectMore(gameDto);
        }
    }

    public List<GameDto> getGameByWeek(GameDto gameDto) {
        return gameRepository.selectByWeek(gameDto);
    }

    public List<GameDto> getGameByTwoWeek(GameDto gameDto) {
        return gameRepository.selectByTwoWeek(gameDto);
    }

    public Map<String, List<GameDto>> getGamesByWeekRandom() {
        List<GameDto> gameList = gameRepository.selectAllByWeek(LocalDateTime.now());

        Map<String, List<GameDto>> gameMap = Optional.ofNullable(gameList)
                .orElse(Collections.emptyList()).stream()
                .collect(Collectors.groupingBy(GameDto::getLeague,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            Collections.shuffle(list); // 리스트를 랜덤하게 섞음
                            List<GameDto> subList = list.subList(0, Math.min(list.size(), 2)); // 최대 2개의 요소만 선택
                            subList.sort(Comparator.comparing(GameDto::getDateTime)); //날짜순으로 정렬
                            return subList;
                        })
                ));

        System.out.println(gameList);

        return gameMap;
    }

    public List<GameDto> getFinishedGameByToday(Map<String,Object> nowAndYester){
        return gameRepository.selectYesterdayGamesByDate(nowAndYester);
    }
}