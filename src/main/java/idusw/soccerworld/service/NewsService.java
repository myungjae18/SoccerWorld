package idusw.soccerworld.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Service
public class NewsService {
    private final RestClient restClient;

    public NewsService(@Qualifier("newsRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Map> getHeadLines() {
        Map data = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/top-headlines")//파라미터로 넘어온 리그코드 사용
                        .queryParam("country", "us")
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
        System.out.println(result);

        return result;
    }
}
