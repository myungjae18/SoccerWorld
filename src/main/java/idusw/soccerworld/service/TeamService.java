package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.PlayerDto;
import idusw.soccerworld.domain.dto.StandingsDto;
import idusw.soccerworld.domain.dto.StatisticsDto;
import idusw.soccerworld.domain.dto.TeamDto;
import idusw.soccerworld.repository.PlayerRepository;
import idusw.soccerworld.repository.StandingsRepository;
import idusw.soccerworld.repository.StatisticsRepository;
import idusw.soccerworld.repository.TeamRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.sql.Date;
import java.util.stream.Collectors;

@Service
public class TeamService {
    TeamRepository teamRepository;
    RestClient restClient;
    PlayerRepository playerRepository;
    StandingsRepository standingsRepository;
    StatisticsRepository statisticsRepository;

    public TeamService(TeamRepository teamRepository,
                       RestClient restClient,
                       PlayerRepository playerRepository,
                       StandingsRepository standingsRepository,
                       StatisticsRepository statisticsRepository) {
        this.teamRepository = teamRepository;
        this.restClient = restClient;
        this.playerRepository = playerRepository;
        this.standingsRepository = standingsRepository;
        this.statisticsRepository = statisticsRepository;
    }


    public ResponseEntity<Map> getTeamInfo(int leagueNum) {

        ResponseEntity<Map> response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/competitions/" + leagueNum + "/teams")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Map.class);

        ResponseEntity<Map> responseData = response;
        System.out.println(responseData);

        return responseData;
    }

    public ResponseEntity<Map> getTeamPastInfo(int leagueNum, String season) {

        ResponseEntity<Map> response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/competitions/" + leagueNum + "/teams")
                        .queryParam("season", season)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Map.class);

        ResponseEntity<Map> responseData = response;
        System.out.println(responseData);

        return responseData;
    }

    public ResponseEntity<Map> getStandingInfo(int leagueNum) {
        ResponseEntity<Map> response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/competitions/" + leagueNum + "/standings")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Map.class);

        ResponseEntity<Map> responseData = response;
        System.out.println(responseData);

        return responseData;
    }

    public ResponseEntity<Map> getStandingPastInfo(int leagueNum, String season) {
        ResponseEntity<Map> response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/competitions/" + leagueNum + "/standings")
                        .queryParam("season", season)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Map.class);

        ResponseEntity<Map> responseData = response;
        System.out.println(responseData);

        return responseData;
    }

    public ResponseEntity<Map> getStatisticsInfo(int leagueNum) {
        ResponseEntity<Map> response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/competitions/" + leagueNum + "/scorers")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Map.class);

        ResponseEntity<Map> responseData = response;
        System.out.println(responseData);

        return responseData;
    }

    public ResponseEntity<Map> getStatisticsPastInfo(int leagueNum, String season) {
        ResponseEntity<Map> response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/competitions/" + leagueNum + "/scorers")
                        .queryParam("season", season)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Map.class);

        ResponseEntity<Map> responseData = response;
        System.out.println(responseData);

        return responseData;
    }

    public int insertTeamInfo(Map<String, Object> teamsData) {

        Map competition = (Map) teamsData.get("competition");
        List<Map> teamList = (List<Map>) teamsData.get("teams");
        List<TeamDto> teamDtoList = new ArrayList<>();
        int result;

        for (Map team : teamList) {

            Map coach = (Map) team.get("coach");
            TeamDto teamDto = TeamDto.builder()
                    .teamId((int) team.get("id"))
                    .name(team.get("shortName").toString())
                    .logo(team.get("crest").toString())
                    .headCoach(coach.get("name").toString())
                    .stadium(team.get("venue").toString())
                    .location(team.get("address").toString())
                    .league(competition.get("name").toString())
                    .clubColor(Optional.ofNullable(team.get("clubColors")).orElse("").toString())
                    .founded(Optional.ofNullable(team.get("founded")).orElse("").toString())
                    .website(team.get("website").toString())
                    .build();
            teamDtoList.add(teamDto);
        }
        System.out.println(teamDtoList);
        result = teamRepository.insertTeam(teamDtoList);

        return result;
    }

    public int insertPlayerInfo(Map<String, Object> teamsData) {

        List<Map> teamList = (List<Map>) teamsData.get("teams");
        List<TeamDto> teamDtoList = new ArrayList<>();
        List<PlayerDto> playerDtoList = new ArrayList<>();
        int result = 0;

        for (Map team : teamList) {
            List<Map> playerList = (List<Map>) team.get("squad");
            TeamDto teamDto = TeamDto.builder()
                    .teamId((int) team.get("id"))
                    .build();
            teamDtoList.add(teamDto);

            for (Map player : playerList) {
                PlayerDto playerDto = PlayerDto.builder()
                        .playerId((int) player.get("id"))
                        .teamDto(teamDto)
                        .name(player.get("name").toString())
                        .nation(player.get("nationality").toString())
                        .position(Optional.ofNullable(player.get("position")).orElse("").toString())
                        .birthDay(Optional.ofNullable(player.get("dateOfBirth")) // birthDay가 null일 가능성 처리
                                .map(Object::toString) // Object -> String 변환
                                .filter(b -> !b.isEmpty()) // 빈 문자열 방지
                                .map(Date::valueOf) // String -> Date 변환
                                .orElse(null)) // null이면 birthDay는 null로 설정
                        .build();
                playerDtoList.add(playerDto);
            }
            result = playerRepository.insertPlayer(playerDtoList);
        }
        return result;
    }

    public int insertStandingInfo(Map<String, Object> standingsData) {

        Map season = (Map) standingsData.get("filters");
        Map competition = (Map) standingsData.get("competition");
        List<Map> firstList = (List<Map>) standingsData.get("standings");
        Map<String, Object> objectS = firstList.get(0);
        List<Map> standingList = (List<Map>) objectS.get("table");
        List<StandingsDto> standingsDtoList = new ArrayList<>();

        int result;

        for (Map standing : standingList) {

            Map teamInfo = (Map) standing.get("team");

            TeamDto teamDto = TeamDto.builder()
                    .teamId((Integer) teamInfo.get("id")).build();


            StandingsDto standingsDto = StandingsDto.builder()
                    .standingsId(season.get("season").toString() + teamInfo.get("id").toString())
                    .position((Integer) standing.get("position"))
                    .teamDto(teamDto)
                    .playedGames((Integer) standing.get("playedGames"))
                    .won((Integer) standing.get("won"))
                    .draw((Integer) standing.get("draw"))
                    .lost((Integer) standing.get("lost"))
                    .points((Integer) standing.get("points"))
                    .goalsFor((Integer) standing.get("goalsFor"))
                    .goalsAgainst((Integer) standing.get("goalsAgainst"))
                    .goalDifference((Integer) standing.get("goalDifference"))
                    .season(season.get("season").toString())
                    .league(competition.get("code").toString())
                    .build();

            standingsDtoList.add(standingsDto);

        }
        System.out.println(standingsDtoList);
        result = standingsRepository.insertStanding(standingsDtoList);

        return result;
    }

    public int insertStatisticsInfo(Map<String, Object> statisticsData) {
        Map season = (Map) statisticsData.get("filters");
        Map competition = (Map) statisticsData.get("competition");
        List<Map> statisticsList = (List<Map>) statisticsData.get("scorers");
        List<StatisticsDto> statisticsDtoList = new ArrayList<>();

        int result;

        for (Map statistics : statisticsList) {
            Map player = (Map) statistics.get("player");
            Map team = (Map) statistics.get("team");

            TeamDto teamDto = TeamDto.builder().teamId((Integer) team.get("id")).build();

            StatisticsDto statisticsDto = StatisticsDto.builder()
                    .statisticsId(season.get("season").toString() + player.get("id").toString())
                    .teamDto(teamDto)
                    .playerId((Integer) player.get("id"))
                    .playerName(player.get("name").toString())
                    .playedMatches((Integer) statistics.get("playedMatches"))
                    .goals((Integer) statistics.get("goals"))
                    .assists((Integer) statistics.get("assists"))
                    .penalties((Integer) statistics.get("penalties"))
                    .season(season.get("season").toString())
                    .league(competition.get("code").toString())
                    .build();
            statisticsDtoList.add(statisticsDto);
        }
        System.out.println(statisticsDtoList);

        result = statisticsRepository.insertStatistics(statisticsDtoList);
        return result;
    }

    //모든 팀 정보를 리스트로 반환하는 메서드
    public List<TeamDto> getAllTeamsByDB() {
        return teamRepository.selectAll();
    }

    //모든 순위 정보를 리스트로 반환하는 메서드
    public List<StandingsDto> getAllStandings() {
        return standingsRepository.selectAll();
    }

    //넘어온 리그와 시즌에 해당하는 순위 정보를 반환하는 메서드
    public List<StandingsDto> getStandingsByLeagueSeason(StandingsDto standingsDto) {
        return standingsRepository.selectByLeagueSeason(standingsDto);
    }

    //현재 시즌 순위 정보를 리스트로 반환하는 메서드
    public Map<String, List<StandingsDto>> getAllCurrentStandings() {
        Integer currentSeason = Year.now().getValue() - 1;

        List<StandingsDto> standingsList = standingsRepository.selectAllBySeason(currentSeason.toString());

        Map<String, List<StandingsDto>> standingsMap =
                Optional.ofNullable(standingsList).orElse(Collections.emptyList()) // null이면 빈 리스트로 대체
                        .stream()
                        .collect(Collectors.groupingBy(StandingsDto::getLeague,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream().collect(Collectors.toList())
                                )
                        ));

        return standingsMap;
    }

    //넘어온 리그와 시즌에 해당하는 선수 통계 리스트 반환
    public List<StatisticsDto> getStatisticsByLeagueSeason(StatisticsDto statisticsDto) {
        return statisticsRepository.selectByLeagueSeason(statisticsDto);
    }

    //teamId를 통해 팀 정보를 조회하는 메서드
    public Map<String, Object> getByPk(String teamId) {
        return teamRepository.selectByPkWithPlayers(Long.valueOf(teamId));
    }
}