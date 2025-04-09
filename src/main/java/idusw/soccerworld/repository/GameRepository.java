package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.GameDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class GameRepository {
    SqlSessionTemplate sessionTemplate;

    public GameRepository(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public List<GameDto> selectAll() {
        return sessionTemplate.selectList("GameName.selectAll");
    }

    public List<GameDto> selectByDate(GameDto gameDto){
        return sessionTemplate.selectList("GameMapper.selectByDate",gameDto);
    }
    public GameDto selectByGameId(int GameId){
        return sessionTemplate.selectOne("GameMapper.selectByGameId",GameId);
    }

    public int insertGames(List<GameDto> gameDtoList){
        int result = sessionTemplate.insert("GameMapper.insertGames",gameDtoList);
        return result;
    }

    public List<GameDto> selectMore(GameDto gameDto) {
        return sessionTemplate.selectList("GameMapper.selectMore", gameDto);
    }

    public List<GameDto> selectByWeek(GameDto gameDto){
        return sessionTemplate.selectList("GameMapper.selectByOneWeek", gameDto);
    }

    public List<GameDto> selectByTwoWeek(GameDto gameDto){
        return sessionTemplate.selectList("GameMapper.selectByTwoWeek",gameDto);
    }

    public List<GameDto> selectAllByWeek(LocalDateTime today){
        return sessionTemplate.selectList("GameMapper.selectAllByWeek", today);
    }

    public List<GameDto> selectYesterdayGamesByDate(Map<String,Object> nowAndYester){
        return sessionTemplate.selectList("GameMapper.selectYesterdayGamesByDate",nowAndYester);
    }
}