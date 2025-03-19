package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.PlayerDto;
import idusw.soccerworld.domain.dto.StandingsDto;
import idusw.soccerworld.domain.dto.StatisticsDto;
import idusw.soccerworld.domain.dto.TeamDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamRepository {
    SqlSessionTemplate sessionTemplate;

    public TeamRepository(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public int insertTeams(List teamList) {
        return sessionTemplate.insert("TeamMapper.insertTeams", teamList);
    }

    public int insertTeam(List<TeamDto> teamDtoList){
        int result = sessionTemplate.insert("TeamMapper.insertTeam",teamDtoList);
        return result;
    }

    public int insertPlayer(List<PlayerDto> playerDtoList){
        int result = sessionTemplate.insert("TeamMapper.insertPlayer", playerDtoList);
        return result;
    }

    public int insertStanding(List<StandingsDto> standingsDtoList) {
        int result = sessionTemplate.insert("TeamMapper.insertStandings",standingsDtoList);
        return result;
    }

    public int insertStatistics(List<StatisticsDto> statisticsDtoList){
        int result = sessionTemplate.insert("TeamMapper.insertStatistics",statisticsDtoList);
        return result;
    }

    public List<TeamDto> selectAll() {
        return sessionTemplate.selectList("TeamMapper.selectAll");
    }
}