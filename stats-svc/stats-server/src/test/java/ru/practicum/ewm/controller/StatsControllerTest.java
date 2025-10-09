package ru.practicum.ewm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.dto.EndpointHitCreateDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.service.StatsService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
class StatsControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private StatsService statsService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createEndpointHit() throws Exception {
        final String APP = "ewm-main-service";
        final String URI = "/events/1";
        final String IP = "192.163.0.1";
        final LocalDateTime TIMESTAMP = LocalDateTime.of(2022, 9, 6, 11, 0, 23);

        final EndpointHitCreateDto endpointHitCreateDto = new EndpointHitCreateDto(APP, URI, IP, TIMESTAMP);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(endpointHitCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(statsService).createEndpointHit(any());
    }

    @Test
    void getStats() throws Exception {
        final List<ViewStatsDto> expected = List.of(
                new ViewStatsDto("test-app-1", "/events/1", 1L),
                new ViewStatsDto("test-app-2", "/events/2", 2L)
        );

        when(statsService.getStats(any(), any(), any(), anyBoolean())).thenReturn(expected);

        mvc.perform(get("/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00&uris=/events/1%26uris=/events/2&unique=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].app", is("test-app-1")))
                .andExpect(jsonPath("$[0].uri", is("/events/1")))
                .andExpect(jsonPath("$[0].hits", is(1)))
                .andExpect(jsonPath("$[1].app", is("test-app-2")))
                .andExpect(jsonPath("$[1].uri", is("/events/2")))
                .andExpect(jsonPath("$[1].hits", is(2)));
        verify(statsService).getStats(any(), any(), any(), anyBoolean());
    }
}