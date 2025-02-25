package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.Member;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private SqlSessionTemplate sessionTemplate;

    public MemberRepository(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public int insertOne(Member member) {
        return sessionTemplate.insert("MemberMapper.insertOne", member);
    }

    public Member loginCheck(String id) {
        return sessionTemplate.selectOne("MemberMapper.loginCheck", id);
    }

    public String selectOneById(String id) {
        return sessionTemplate.selectOne("MemberMapper.selectById", id);
    }

    public String selectOneByNickname(String nickname) {
        return sessionTemplate.selectOne("MemberMapper.selectByNickname", nickname);
    }
}
