package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.Team;
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

    public List<Team> selectAll() {
        return sessionTemplate.selectList("TeamMapper.selectAll");
    }
}
