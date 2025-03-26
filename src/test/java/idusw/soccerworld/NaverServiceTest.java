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
class NaverServiceTest {
    private RestClient restClient = RestClient.builder()
            .baseUrl("https://openapi.naver.com/v1")
            .defaultHeader("X-Naver-Client-Id", "Anl9OQUQ2GNHGZMLOVe4")
            .defaultHeader("X-Naver-Client-Secret", "DgyDZCYCJw")
            .build();
    @Test
    void getHeadLine() {
        Map data = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/news.json")//파라미터로 넘어온 리그코드 사용
                        .queryParam("query", "축구")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {//4백번대 예외 처리
                    throw new RestClientException("Server error: " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {//5백번대 예외 처리
                    throw new RestClientException("Server error: " + res.getStatusCode());
                })
                .body(Map.class);

        System.out.println(data);
    }
}
