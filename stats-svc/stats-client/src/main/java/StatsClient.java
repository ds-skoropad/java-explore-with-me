import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import ru.practicum.ewm.dto.EndpointHitCreateDto;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class StatsClient {
    private final RestClient restClient;

    @Autowired
    public StatsClient(@Value("${stats-server.url:http://stats-server:9090}") String statServerUrl,
                       RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(statServerUrl)
                .build();
    }

    public void createEndpointHit(EndpointHitCreateDto endpointHitCreateDto) {
        restClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(endpointHitCreateDto)
                .retrieve()
                .toBodilessEntity();
    }

    public List<ViewStatsDto> getStats(Instant start, Instant end, Optional<List<String>> uris, Boolean unique) {
        return restClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path("/stats")
                            .queryParam("start", start)
                            .queryParam("end", end)
                            .queryParam("unique", unique);
                    uris.ifPresent(l -> l.forEach(uri -> uriBuilder.queryParam("uris", uri)));
                    return builder.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

}
