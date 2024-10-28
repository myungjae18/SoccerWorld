package idusw.soccerworld.controller;

import idusw.soccerworld.domain.fixtureApi.Fixture;
import idusw.soccerworld.domain.fixtureApi.FixtureTeam;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class fixtureController {
    LocalDate today = LocalDate.now();

    // 날짜 포맷 설정 (예: yyyy년 MM월 dd일)
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // 포맷팅된 문자열로 변환
    String stringToday = today.format(formatter);

    public String changeDate(String dateString) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateString);
        ZonedDateTime kstDateTime = offsetDateTime.atZoneSameInstant(ZoneId.of("Asia/Seoul"));

        // 원하는 형식으로 포맷 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd. (E) a hh:mm", Locale.KOREA);

        // 포맷팅된 문자열로 변환
        String stringDate = kstDateTime.format(formatter);

        return stringDate;
    }


    @GetMapping("game/aaa")
    public String getMy(Model model) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity
                .get("https://v3.football.api-sports.io/fixtures?date=" + stringToday + "&league=39&season=2024").header("x-rapidapi-key", "73b2b917e94580c8bd9bb06ab1b77f14").build();

        String result = restTemplate.exchange(req, String.class).getBody(); //결과를 String을 받음 .getBody로 Body부분 얻음
        JSONObject jsonObject = new JSONObject(result);
        JSONArray response = jsonObject.getJSONArray("response");//배열 (여러경기)
        System.out.println(result);

        List<Fixture> gameList = new ArrayList<>();
        JSONObject teams = null;
        JSONObject home = null;
        JSONObject away = null;
        JSONObject fixtureInfo = null;
        JSONObject league = null;
        JSONObject status = null;
        JSONObject goals = null;

        if (response.length() != 0) {
            for (int i = 0; i < response.length(); i++) {
                Fixture game = new Fixture();
                FixtureTeam homeTeam = new FixtureTeam();
                FixtureTeam awayTeam = new FixtureTeam();
                JSONObject fixture = response.getJSONObject(i); //response패러미터 첫번째 객체들 구함
                fixtureInfo = fixture.getJSONObject("fixture");
                league = fixture.getJSONObject("league");
                status = fixtureInfo.getJSONObject("status");
                teams = fixture.getJSONObject("teams"); //teams이라는 필드 객체
                home = teams.getJSONObject("home"); //teams안에 있는 필드 객체
                away = teams.getJSONObject("away"); // --
                goals = fixture.getJSONObject("goals");
                homeTeam.setId(home.getInt("id")); // home의 필드 id를 가져옴
                homeTeam.setName(home.getString("name"));
                homeTeam.setLogo(home.getString("logo"));
                homeTeam.setGoals(goals.optInt("home"));

                awayTeam.setId(away.getInt("id"));
                awayTeam.setName(away.getString("name"));
                awayTeam.setLogo(away.getString("logo"));
                awayTeam.setGoals(goals.optInt("away")); //경기 시작전이면 null로 표시되기때문에 opInt 사용

                game.setDate(changeDate(fixtureInfo.getString("date")));
                game.setStatus(status.getString("long"));
                game.setHome(homeTeam);
                game.setAway(awayTeam);
                game.setRound(league.getString("round"));
                game.setLeague(league.getString("name"));
                game.setLeagueLogo(league.getString("logo"));
                gameList.add(game);
            }
        } else {
            gameList = new ArrayList<>();
        }


        model.addAttribute("fixtures", gameList);
        return "game/live";
    }

    @GetMapping("fixture/premierleague-schedule")
    public String goPremierLeague() {
        return "fixture/premierleague-schedule";
    }

    @GetMapping("fixture/laliga-schedule")
    public String goLaliga() { return "fixture/laliga-schedule"; }

    @GetMapping("fixture/seriea-schedule")
    public String goSeriaA() { return "fixture/seriea-schedule"; }

    @GetMapping("fixture/bundesliga-schedule")
    public String goBundesliga() { return "fixture/bundesliga-schedule"; }

    @GetMapping("/schedule")
    @ResponseBody
    public List<Fixture> getFixture(@RequestParam(required = false, value = "selectedDate")String date,
                                    @RequestParam(required = false, value = "leagueNum")String leagueNum) {
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity
                .get("https://v3.football.api-sports.io/fixtures?date=" + date + "&league=" + leagueNum + "&season=2024").header("x-rapidapi-key", "73b2b917e94580c8bd9bb06ab1b77f14").build();

        String result = restTemplate.exchange(req, String.class).getBody(); //결과를 String을 받음 .getBody로 Body부분 얻음
        JSONObject jsonObject = new JSONObject(result);
        JSONArray response = jsonObject.getJSONArray("response");//배열 (여러경기)
        System.out.println(result);

        List<Fixture> gameList = new ArrayList<>();
        JSONObject teams = null;
        JSONObject home = null;
        JSONObject away = null;
        JSONObject fixtureInfo = null;
        JSONObject league = null;
        JSONObject status = null;
        JSONObject goals = null;

        if (response != null) {
            for (int i = 0; i < response.length(); i++) {
                Fixture game = new Fixture();
                FixtureTeam homeTeam = new FixtureTeam();
                FixtureTeam awayTeam = new FixtureTeam();
                JSONObject fixture = response.getJSONObject(i); //response패러미터 첫번째 객체들 구함
                fixtureInfo = fixture.getJSONObject("fixture");
                league = fixture.getJSONObject("league");
                status = fixtureInfo.getJSONObject("status");
                teams = fixture.getJSONObject("teams"); //teams이라는 필드 객체
                home = teams.getJSONObject("home"); //teams안에 있는 필드 객체
                away = teams.getJSONObject("away"); // --
                goals = fixture.getJSONObject("goals");
                homeTeam.setId(home.getInt("id")); // home의 필드 id를 가져옴
                homeTeam.setName(home.getString("name"));
                homeTeam.setLogo(home.getString("logo"));
                homeTeam.setGoals(goals.optInt("home"));

                awayTeam.setId(away.getInt("id"));
                awayTeam.setName(away.getString("name"));
                awayTeam.setLogo(away.getString("logo"));
                awayTeam.setGoals(goals.optInt("away")); //경기 시작전이면 null로 표시되기때문에 opInt 사용

                game.setDate(changeDate(fixtureInfo.getString("date")));
                game.setStatus(status.getString("long"));
                game.setHome(homeTeam);
                game.setAway(awayTeam);
                game.setRound(league.getString("round"));
                game.setLeague(league.getString("name"));
                game.setLeagueLogo(league.getString("logo"));
                gameList.add(game);
            }
            return gameList;
        } else {
            return gameList;
        }

    }
}
