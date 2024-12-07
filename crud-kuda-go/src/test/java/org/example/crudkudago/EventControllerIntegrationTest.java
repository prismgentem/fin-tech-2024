package org.example.crudkudago;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.crudkudago.model.EventRequest;
import org.example.crudkudago.model.EventResponse;
import org.example.crudkudago.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final UUID EVENT_ID = UUID.fromString("b8f2fc72-bbf7-46a0-b4a5-d3f223a9e5b1");

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    static {
        postgresContainer.start();
    }

    @Test
    void shouldReturnAllEvents() throws Exception {
        var eventResponse = new EventResponse();
        eventResponse.setId(EVENT_ID);
        eventResponse.setName("Test Event");

        when(eventService.getAllEvents(null, null, null, null)).thenReturn(Collections.singletonList(eventResponse));

        MvcResult mvcResult = mockMvc.perform(get("/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Test Event"))
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        EventResponse[] events = objectMapper.readValue(jsonResponse, EventResponse[].class);
        assertThat(events).hasSize(1);
        assertThat(events[0].getName()).isEqualTo("Test Event");

        verify(eventService, times(1)).getAllEvents(null, null, null, null);
    }

    @Test
    void shouldReturnEventById() throws Exception {
        var eventResponse = new EventResponse();
        eventResponse.setId(EVENT_ID);
        eventResponse.setName("Test Event");

        when(eventService.getEventById(EVENT_ID)).thenReturn(eventResponse);

        MvcResult mvcResult = mockMvc.perform(get("/events/{id}", EVENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        EventResponse responseEvent = objectMapper.readValue(jsonResponse, EventResponse.class);
        assertThat(responseEvent.getName()).isEqualTo("Test Event");
        assertThat(responseEvent.getId()).isEqualTo(EVENT_ID);

        verify(eventService, times(1)).getEventById(EVENT_ID);
    }

    @Test
    void shouldCreateEvent() throws Exception {
        EventRequest newEventRequest = createRequest();

        String eventJson = objectMapper.writeValueAsString(newEventRequest);

        var createdEventResponse = new EventResponse();
        createdEventResponse.setId(EVENT_ID);
        createdEventResponse.setName(newEventRequest.getName());

        when(eventService.createEvent(any(EventRequest.class))).thenReturn(createdEventResponse);

        MvcResult mvcResult = mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        EventResponse responseEvent = objectMapper.readValue(jsonResponse, EventResponse.class);
        assertThat(responseEvent.getName()).isEqualTo(newEventRequest.getName());
        assertThat(responseEvent.getId()).isNotNull();

        verify(eventService, times(1)).createEvent(any(EventRequest.class));
    }

    @Test
    void shouldUpdateEvent() throws Exception {
        EventRequest updatedEventRequest = createRequest();

        String eventJson = objectMapper.writeValueAsString(updatedEventRequest);

        var updatedEventResponse = new EventResponse();
        updatedEventResponse.setId(EVENT_ID);
        updatedEventResponse.setName(updatedEventRequest.getName());

        when(eventService.updateEvent(eq(EVENT_ID), any(EventRequest.class))).thenReturn(updatedEventResponse);

        MvcResult mvcResult = mockMvc.perform(put("/events/{id}", EVENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        EventResponse responseEvent = objectMapper.readValue(jsonResponse, EventResponse.class);
        assertThat(responseEvent.getName()).isEqualTo(updatedEventRequest.getName());
        assertThat(responseEvent.getId()).isEqualTo(EVENT_ID);

        verify(eventService, times(1)).updateEvent(eq(EVENT_ID), any(EventRequest.class));
    }

    @Test
    void shouldDeleteEvent() throws Exception {
        doNothing().when(eventService).deleteEventById(EVENT_ID);

        MvcResult mvcResult = mockMvc.perform(delete("/events/{id}", EVENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResponse).isEmpty();

        verify(eventService, times(1)).deleteEventById(EVENT_ID);
    }

    private EventRequest createRequest(){
        var request = new EventRequest();
        request.setName("Test Event");
        request.setDate(LocalDate.now().toString());
        request.setPlaceId(UUID.randomUUID());
        return request;
    }
}
