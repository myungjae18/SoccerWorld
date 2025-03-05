package idusw.soccerworld.domain.dto;

import lombok.Data;

@Data
public class PostDto {
    private int category_id;

    private int post_id;

    private int member_id;

    private String  title;

    private String  content;

    private int up_count = 0;

    private int down_count = 0;

    private int view_count = 0;

    private String  regdate;

    private String  picture;

}