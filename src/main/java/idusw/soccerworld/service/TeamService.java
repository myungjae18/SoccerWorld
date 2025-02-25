package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.TeamDto;
import idusw.soccerworld.repository.TeamRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TeamService {
    TeamRepository teamRepository;
    RestClient restClient;
    public TeamService(TeamRepository teamRepository,
                       RestClient restClient){
        this.teamRepository = teamRepository;
        this.restClient = restClient;
    }

    public ResponseEntity<Map> getTeamInfo(int leagueNum){

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
    public int insertTeamInfo(Map<String, Object> teamsData){

        Map competition = (Map) teamsData.get("competition");
        List<Map> teamList = (List<Map>) teamsData.get("teams");
        List<TeamDto> teamDtoList = new ArrayList<>();
        int result;

        for(Map team : teamList) {
            Map coach = (Map) team.get("coach");
            TeamDto teamDto = TeamDto.builder()
                    .teamId((int) team.get("id"))
                    .name(team.get("shortName").toString())
                    .logo(team.get("crest").toString())
                    .headCoach(coach.get("name").toString())
                    .stadium(team.get("venue").toString())
                    .location(team.get("address").toString())
                    .league(competition.get("name").toString())
                    .build();

            teamDtoList.add(teamDto);
        }

        result = teamRepository.insertTeam(teamDtoList);

        return result;
    }

    //모든 팀 정보를 리스트로 반환하는 메서드
    public List<TeamDto> getAllTeamsByDB() {
        return teamRepository.selectAll();
    }
}

