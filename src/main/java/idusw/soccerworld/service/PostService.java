package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.CategoryDto;
import idusw.soccerworld.domain.dto.PostDto;
import idusw.soccerworld.repository.CategoryRepository;
import idusw.soccerworld.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

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

//    public Map<String, List<PostDto>> selectAllMap() {
//        List<PostDto> postList = postRepository.selectAll();
//        List<CategoryDto> categoryList = categoryRepository.findAll();
//        Map<String, List<PostDto>> postMap = new HashMap<>();
////
////        Map<Integer, List<PostDto>> groupedPosts = postList.stream()
////                .collect(Collectors.groupingBy(
////                        PostDto::getCategory_id, // category_id로 그룹화
////                        Collectors.collectingAndThen(
////                                Collectors.toList(),
////                                list -> list.stream()
////                                        .sorted(Comparator.comparingInt(PostDto::getUp_count).reversed())
////                                        .limit(3)
////                                        .collect(Collectors.toList())
////                        )
////                ));
//
//
//
//
//        return null;
//    }
}