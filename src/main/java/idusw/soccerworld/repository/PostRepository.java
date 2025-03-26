package idusw.soccerworld.repository;


import idusw.soccerworld.domain.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private  final SqlSessionTemplate sql;

    public List<PostDto> selectedCategory(int categoryId) {
        return sql.selectList("postMapper.selectedCategory", categoryId);
    }

    public void create(PostDto postDTO) {
        sql.insert("postMapper.create", postDTO);
    }

    public void updateViewCount(int postId) {
        sql.update("postMapper.updateViewCount", postId);
    }

    public PostDto findById(int postId) {
        return sql.selectOne("postMapper.findById", postId);
    }

    public void updatePost(PostDto postDTO, int postId) {
        postDTO.setPost_id(postId);
        sql.update("postMapper.updatePost", postDTO);
    }

    public void deletePost(int postId) {
        sql.delete("postMapper.deletePost", postId);
    }

    public List<PostDto> selectAll() {
        return sql.selectList("postMapper.selectAll");
    }
}