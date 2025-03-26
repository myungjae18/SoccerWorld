package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.StatisticsDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatisticsRepository {
    SqlSessionTemplate sessionTemplate;
    public StatisticsRepository(SqlSessionTemplate sqlSessionTemplate){
        this.sessionTemplate = sqlSessionTemplate;
    }

    public int insertStatistics(List<StatisticsDto> statisticsDtoList){
        int result = sessionTemplate.insert("StatisticsMapper.insertStatistics",statisticsDtoList);
        return result;
    }
}