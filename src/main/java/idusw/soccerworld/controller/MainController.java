package idusw.soccerworld.controller;

import idusw.soccerworld.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainController {
    final CategoryRepository categoryRepository;

    public MainController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/index")
    public String goIndex() {
        System.out.println(categoryRepository.selectAll());
        return "/main/index";
    }

}
