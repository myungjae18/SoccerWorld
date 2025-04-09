package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.PlayerDto;
import idusw.soccerworld.domain.dto.StandingsDto;
import idusw.soccerworld.domain.dto.StatisticsDto;
import idusw.soccerworld.domain.dto.TeamDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    public List<TeamDto> selectAll() {
        return sessionTemplate.selectList("TeamMapper.selectAll");
    }

    public Map<String, Object> selectByPkWithPlayers(Long teamId) {
        return sessionTemplate.selectOne("TeamMapper.selectByPkWithPlayers", teamId);
    }
}