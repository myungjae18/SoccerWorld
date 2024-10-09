package idusw.soccerworld.repository;

import idusw.soccerworld.domain.entity.Category;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {
    SqlSessionTemplate sessionTemplate;

    public CategoryRepository(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public List selectAll() {
        return sessionTemplate.selectList("Category.selectAll");
    }
}
