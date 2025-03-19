package idusw.soccerworld.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    private String footballKey = "32c1c11298b9489898887d2302b4fa2f";
    private String newsKey = "911c8209f6de4e6cb610833cde17902f";

    //기본 url과 key가 포함된 api-football용 RestClient Bean 생성
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://api.football-data.org/v4")
                .defaultHeader("x-Auth-Token", footballKey)
                .build();
    }

    //기본 url과 key가 포함된 news-api용 RestClient Bean 생성
    @Bean
    public RestClient newsRestClient() {
        return RestClient.builder()
                .baseUrl("https://newsapi.org/v2")
                .defaultHeader("X-Api-Key", newsKey)
                .build();
    }
}
