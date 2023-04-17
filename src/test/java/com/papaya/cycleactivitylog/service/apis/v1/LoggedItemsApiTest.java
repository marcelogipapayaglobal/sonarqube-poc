package com.papaya.cycleactivitylog.service.apis.v1;

import com.papaya.cycleactivitylog.service.apis.services.PrimaryLoggedItemsApiService;
import com.papaya.cycleactivitylog.service.apis.v1.controllers.LoggedItemsApiController;
import com.papaya.cycleactivitylog.service.data.LoggedItemsReadRepository;
import com.papaya.cycleactivitylog.service.model.LoggedItem;
import com.papaya.cycleactivitylog.service.model.LoggedItemEventType;
import com.papaya.cycleactivitylog.service.model.LoggedItemSeverity;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        LoggedItemsApiController.class,
        PrimaryLoggedItemsApiService.class
})
public class LoggedItemsApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoggedItemsReadRepository readRepository;

    @Test
    public void one_item() throws Exception {
        String cycleId = "1";

        var item = new LoggedItem(
                "1",
                "hash",
                LocalDateTime.of(2023, 1, 1, 1, 1, 1, 20000000),
                LocalDateTime.of(2023, 2, 2, 2, 2, 2, 20000000),
                "source",
                "event",
                "state",
                LoggedItemSeverity.Info,
                LoggedItemEventType.Business,
                "user",
                "summary"
        );

        Mockito.when(readRepository.findByCycleId(item.getCycleId())).thenReturn(List.of(item));
        this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("cycle-id", item.getCycleId())
                        .param("size", "2")
                        .param("page", "0")
                        .param("event-types", item.getEventType().name())
                        .param("sources", item.getSource())
                        .param("states", item.getState())
                        .param("from", item.getWhen().toString())
                        .param("to", item.getWhen().toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("""
                        {
                            "page":0,
                            "size":2,
                            "totalNumberOfElements":1,
                            "hasNext":false,
                            "hasPrevious":false,
                            "content":[
                                {"cycleId":"1","hash": "hash","when":"2023-01-01T01:01:01.020","occurrence":"2023-02-02T02:02:02.020","source":"source","event":"event","state":"state","severity":"Info","eventType":"Business","user":"user","summary":"summary"}
                            ],
                            "numberOfElements":1
                        }
                        """
                , true));

        var page = this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("cycle-id", item.getCycleId())
                        .param("size", "2")
                        .param("page", "1")).andReturn().getResponse().getContentAsString();
        this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("cycle-id", item.getCycleId())
                        .param("size", "2")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("""
                        {
                            "page":1,
                            "size":2,
                            "totalNumberOfElements":1,
                            "hasNext":false,
                            "hasPrevious":true,
                            "content":[],
                            "numberOfElements":0
                        }
                        """
                ));

    }

    @Test
    public void one_item_with_null_state() throws Exception {
        String cycleId = "1";

        var item = new LoggedItem(
                "1",
                "hash",
                LocalDateTime.of(2023, 1, 1, 1, 1, 1, 20000000),
                LocalDateTime.of(2023, 2, 2, 2, 2, 2, 20000000),
                "source",
                "event",
                null,
                LoggedItemSeverity.Info,
                LoggedItemEventType.Business,
                "user",
                "summary"
        );

        Mockito.when(readRepository.findByCycleId(item.getCycleId())).thenReturn(List.of(item));
        this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("cycle-id", item.getCycleId())
                        .param("size", "2")
                        .param("page", "0")
                        .param("event-types", item.getEventType().name())
                        .param("sources", item.getSource())
                        .param("states", item.getState())
                        .param("from", item.getWhen().toString())
                        .param("to", item.getWhen().toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("""
                        {
                            "page":0,
                            "size":2,
                            "totalNumberOfElements":1,
                            "hasNext":false,
                            "hasPrevious":false,
                            "content":[
                                {"cycleId":"1","hash": "hash","when":"2023-01-01T01:01:01.020","occurrence":"2023-02-02T02:02:02.020","source":"source","event":"event","state":null,"severity":"Info","eventType":"Business","user":"user","summary":"summary"}
                            ],
                            "numberOfElements":1
                        }
                        """
                        , true));

        var page = this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("cycle-id", item.getCycleId())
                        .param("size", "2")
                        .param("page", "1")).andReturn().getResponse().getContentAsString();
        this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("cycle-id", item.getCycleId())
                        .param("size", "2")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("""
                        {
                            "page":1,
                            "size":2,
                            "totalNumberOfElements":1,
                            "hasNext":false,
                            "hasPrevious":true,
                            "content":[],
                            "numberOfElements":0
                        }
                        """
                ));

    }

    @Test
    public void missing_cycleId_should_fail_with_BadRequest() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("size", "2")
                        .param("page", "0"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void missing_page_or_size_should_fail_with_BadRequest() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("cycle-id", "1")
                        .param("size", "2"))
                .andExpect(status().isBadRequest());

        this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("cycle-id", "1")
                        .param("page", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void negative_page_or_size_should_fail_with_BadRequest() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("cycle-id", "1")
                        .param("page", "2")
                        .param("size", "-2"))
                .andExpect(status().isBadRequest());

        this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("cycle-id", "1")
                        .param("page", "-2")
                        .param("size", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void invalid_event_type_should_fail_with_BadRequest() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/logged-items/searches/by-cycle")
                        .param("cycle-id", "1")
                        .param("page", "2")
                        .param("size", "2")
                        .param("event-types", "A")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void invalid_period_should_fail() throws Exception {
        assertThrows(ServletException.class, () -> {
                    this.mockMvc
                            .perform(get("/api/v1/logged-items/searches/by-cycle")
                                    .param("cycle-id", "1")
                                    .param("size", "2")
                                    .param("page", "0")
                                    .param("from", "2023-02-01T01:01:01.020")
                                    .param("to", "2023-01-01T01:01:01.020")
                            );
                }
        );
    }
}
