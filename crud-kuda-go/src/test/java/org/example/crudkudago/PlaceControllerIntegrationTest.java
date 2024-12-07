package org.example.crudkudago;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.crudkudago.model.PlaceRequest;
import org.example.crudkudago.model.PlaceResponse;
import org.example.crudkudago.service.PlaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class PlaceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceService placeService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final UUID PLACE_ID = UUID.fromString("b8f2fc72-bbf7-46a0-b4a5-d3f223a9e5b1");

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    static {
        postgresContainer.start();
    }

    @Test
    void shouldReturnAllPlaces() throws Exception {
        var placeResponse = new PlaceResponse();
        placeResponse.setId(PLACE_ID);
        placeResponse.setName("Test Place");

        when(placeService.getAllPlaces()).thenReturn(Collections.singletonList(placeResponse));

        MvcResult mvcResult = mockMvc.perform(get("/places")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Test Place"))
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PlaceResponse[] places = objectMapper.readValue(jsonResponse, PlaceResponse[].class);
        assertThat(places).hasSize(1);
        assertThat(places[0].getName()).isEqualTo("Test Place");

        verify(placeService, times(1)).getAllPlaces();
    }

    @Test
    void shouldReturnPlaceById() throws Exception {
        var placeResponse = new PlaceResponse();
        placeResponse.setId(PLACE_ID);
        placeResponse.setName("Test Place");

        when(placeService.getPlaceById(PLACE_ID)).thenReturn(placeResponse);

        MvcResult mvcResult = mockMvc.perform(get("/places/{id}", PLACE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PlaceResponse responsePlace = objectMapper.readValue(jsonResponse, PlaceResponse.class);
        assertThat(responsePlace.getName()).isEqualTo("Test Place");
        assertThat(responsePlace.getId()).isEqualTo(PLACE_ID);

        verify(placeService, times(1)).getPlaceById(PLACE_ID);
    }

    @Test
    void shouldCreatePlace() throws Exception {
        PlaceRequest newPlaceRequest = createPlaceRequest();
        String placeJson = objectMapper.writeValueAsString(newPlaceRequest);

        var createdPlaceResponse = new PlaceResponse();
        createdPlaceResponse.setId(PLACE_ID);
        createdPlaceResponse.setName("testName");

        when(placeService.createPlace(any(PlaceRequest.class))).thenReturn(createdPlaceResponse);

        MvcResult mvcResult = mockMvc.perform(post("/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(placeJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PlaceResponse responsePlace = objectMapper.readValue(jsonResponse, PlaceResponse.class);
        assertThat(responsePlace.getName()).isEqualTo("testName");
        assertThat(responsePlace.getId()).isNotNull();

        verify(placeService, times(1)).createPlace(any(PlaceRequest.class));
    }

    @Test
    void shouldUpdatePlace() throws Exception {
        PlaceRequest updatedPlaceRequest = createPlaceRequest();
        String placeJson = objectMapper.writeValueAsString(updatedPlaceRequest);

        var updatedPlaceResponse = new PlaceResponse();
        updatedPlaceResponse.setId(PLACE_ID);
        updatedPlaceResponse.setName("testName");

        when(placeService.updatePlace(any(PlaceRequest.class), eq(PLACE_ID))).thenReturn(updatedPlaceResponse);

        MvcResult mvcResult = mockMvc.perform(put("/places/{id}", PLACE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(placeJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PlaceResponse responsePlace = objectMapper.readValue(jsonResponse, PlaceResponse.class);
        assertThat(responsePlace.getName()).isEqualTo("testName");
        assertThat(responsePlace.getId()).isEqualTo(PLACE_ID);

        verify(placeService, times(1)).updatePlace(any(PlaceRequest.class), eq(PLACE_ID));
    }

    @Test
    void shouldDeletePlace() throws Exception {
        doNothing().when(placeService).deletePlaceById(PLACE_ID);

        MvcResult mvcResult = mockMvc.perform(delete("/places/{id}", PLACE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResponse).isEmpty();

        verify(placeService, times(1)).deletePlaceById(PLACE_ID);
    }

    private PlaceRequest createPlaceRequest() {
        var request = new PlaceRequest();
        request.setName("testName");
        request.setCity("testCity");
        request.setAddress("testAddress");
        return request;
    }
}
