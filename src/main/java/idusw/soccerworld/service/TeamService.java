package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.Team;
import idusw.soccerworld.repository.TeamRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TeamService {
    private RestClient restClient;
    private TeamRepository teamRepository;

    public TeamService(RestClient restClient, TeamRepository teamRepository) {
        this.restClient = restClient;
        this.teamRepository = teamRepository;
    }

    //각 리그의 팀 정보를 api에서 가져와 DB에 저장하는 메서드
    public void getTeamsByApi(String league, String leagueName) {
        //generic이 지정되지 않은 map으로 받음
        ResponseEntity<Map> data = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v4/competitions/"+league+"/teams")//파라미터로 넘어온 리그코드 사용
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {//4백번대 예외 처리
                    throw new RestClientException("Client error: " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {//5백번대 예외 처리
                    throw new RestClientException("Server error: " + res.getStatusCode());
                })
                .toEntity(Map.class);

        List<Map> teamsInfo = (List<Map>)data.getBody().get("teams"); //teams가 Map형으로 된 배열이므로

        System.out.println(teamsInfo);

        List<Team> teamList = new ArrayList<>(); //Team DTO를 저장할 리스트 생성

        //리스트에서 Map을 꺼냄. Map에 제너릭을 지정하지 않아 다시 가공해야 할 경우 자료형을 지정하여 뽑으면 된다.
        for(Map result : teamsInfo) {
            Map coach = (Map) result.get("coach");
            Map area = (Map) result.get("area");

            //dto build
            Team team = Team.builder()
                    .teamId((Integer) result.get("id"))
                    .name(result.get("shortName").toString())
                    .league(leagueName)
                    .logo(result.get("crest").toString())
                    .headCoach(coach.get("name").toString())
                    .stadium(result.get("venue").toString())
                    .location(area.get("name").toString())
                    .build();

            System.out.println(team.getName());
            teamList.add(team);//생성한 dto 저장
        }

        //list로 한 번에 insert
        teamRepository.insertTeams(teamList);
    }

    //모든 팀 정보를 리스트로 반환하는 메서드
    public List<Team> getAllTeamsByDB() {
        return teamRepository.selectAll();
    }
}
