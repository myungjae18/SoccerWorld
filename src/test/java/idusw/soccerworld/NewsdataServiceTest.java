package idusw.soccerworld;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class NewsdataServiceTest {
    private RestClient restClient = RestClient.builder()
            .baseUrl("https://newsdata.io/api/1/latest")
            .defaultHeader("X-ACCESS-KEY", "pub_80524b86e9f27e671b7e48a8a23959647c981")
            .build();

    @Test
    void getNews() {
        Map data = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", "football")
                        .queryParam("language", "korean")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {//4백번대 예외 처리
                    throw new RestClientException("Server error: " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {//5백번대 예외 처리
                    throw new RestClientException("Server error: " + res.getStatusCode());
                })
                .body(Map.class);

        List<Map> results = (List<Map>)data.get("results");

        System.out.println(data);
    }
}
