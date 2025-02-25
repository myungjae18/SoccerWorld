package idusw.soccerworld.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CommunityController {
    @RequestMapping(method = RequestMethod.GET,value = "/")
    public String goIndex(){
        return "community/index";
    }

    @GetMapping("community/meme_list")
    public String meme_list(){
        return "community/meme_list";
    }

    @GetMapping("community/meme_detail")
    public String meme_detail(){
        return "community/meme_detail";
    }

    @GetMapping("community/free_list")
    public String free_list(){
        return "community/free_list";
    }

    @GetMapping("community/free_detail")
    public String free_detail(){
        return "community/free_detail";
    }

    @GetMapping("community/debate_list")
    public String debate_list(){
        return "community/debate_list";
    }

    @GetMapping("community/debate_detail")
    public String debate_detail(){
        return "community/debate_detail";
    }

    @GetMapping("community/history_list")
    public String history_list(){
        return "community/history_list";
    }

    @GetMapping("/community/history_detail")
    public String history_detail(){
        return "community/history_detail";
    }

    @GetMapping("/community/prospect_list")
    public String prospect_list(){
        return "community/prospect_list";
    }

    @GetMapping("/community/prospect_detail")
    public String prospect_detail(){
        return "community/prospect_detail";
    }

}
