package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.MemberDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private SqlSessionTemplate sessionTemplate;

    public MemberRepository(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    //insert methods
    public int insertOne(MemberDto memberDto) {
        return sessionTemplate.insert("MemberMapper.insertOne", memberDto);
    }

    //select methods
    public MemberDto loginCheck(String id) {
        return sessionTemplate.selectOne("MemberMapper.loginCheck", id);
    }

    public String selectOneById(String id) {
        return sessionTemplate.selectOne("MemberMapper.selectById", id);
    }

    public String selectOneByNickname(String nickname) {
        return sessionTemplate.selectOne("MemberMapper.selectByNickname", nickname);
    }

    public MemberDto selectByMemberId(long memberId) {
        return sessionTemplate.selectOne("MemberMapper.selectByMemberId", memberId);
    }

    //update methods
    public int update(MemberDto memberDto) {
        return sessionTemplate.update("MemberMapper.update", memberDto);
    }

    public int updatePassword(MemberDto memberDto) {
        return sessionTemplate.update("MemberMapper.updatePassword", memberDto);
    }

    public int updateTeam(MemberDto memberDto) {
        return sessionTemplate.update("MemberMapper.updateTeam", memberDto);
    }

    public int updateNickname(MemberDto memberDto) {
        return sessionTemplate.update("MemberMapper.updateNickname", memberDto);
    }

    public int updatePointGetByMemberId(Long memberId) {
        return sessionTemplate.update("MemberMapper.updatePointGetByMemberId",memberId);
    }

    public int updatePointLoseByMemberId(Long memberId) {
        return sessionTemplate.update("MemberMapper.updatePointLoseByMemberId",memberId);
    }
}
