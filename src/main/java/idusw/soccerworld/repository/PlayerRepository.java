package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.PlayerDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerRepository {
    SqlSessionTemplate sessionTemplate;

    public PlayerRepository(SqlSessionTemplate sessionTemplate){
        this.sessionTemplate = sessionTemplate;
    }

    public int insertPlayer(List<PlayerDto> playerDtoList){
        int result = sessionTemplate.insert("PlayerMapper.insertPlayer", playerDtoList);
        return result;
    }
}