package idusw.soccerworld;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@SpringBootTest
class NewsServiceTest {
    private RestClient restClient = RestClient.builder()
                .baseUrl("https://newsapi.org/v2")
                .defaultHeader("X-Api-Key", "911c8209f6de4e6cb610833cde17902f")
                .build();
    @Test
    void getHeadLine() {
        Map data = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/top-headlines")//파라미터로 넘어온 리그코드 사용
                        .queryParam("category", "sports")
                        .queryParam("country", "it")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {//4백번대 예외 처리
                    throw new RestClientException("Server error: " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {//5백번대 예외 처리
                    throw new RestClientException("Server error: " + res.getStatusCode());
                })
                .body(Map.class);

        List<Map> result = (List<Map>)data.get("articles");

        System.out.println(data.get("articles"));
    }
}
