package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.StandingsDto;
import lombok.AllArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class StandingsRepository {
    private SqlSessionTemplate sessionTemplate;

    public int insertStanding(List<StandingsDto> standingsDtoList) {
        int result = sessionTemplate.insert("StandingsMapper.insertStandings",standingsDtoList);
        return result;
    }

    public List<StandingsDto> selectAll() {
        return sessionTemplate.selectList("StandingsMapper.selectAll");
    }

    public List<StandingsDto> selectAllBySeason(String season) {
        return sessionTemplate.selectList("StandingsMapper.selectAllBySeason", season);
    }
}
