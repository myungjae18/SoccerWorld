package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.PostDto;
import idusw.soccerworld.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<PostDto> selectedCategory(int categoryId) {
        return postRepository.selectedCategory(categoryId);
    }

    public void create(PostDto postDTO) {
        postRepository.create(postDTO);
    }

    public void updateViewCount(int postId) {
        postRepository.updateViewCount(postId);
    }

    public PostDto findById(int postId) {
        return postRepository.findById(postId);
    }

    public void updatePost(PostDto postDTO, int postId) {
        postRepository.updatePost(postDTO, postId);
    }

    public void deletePost(int postId) {
        postRepository.deletePost(postId);
    }
}