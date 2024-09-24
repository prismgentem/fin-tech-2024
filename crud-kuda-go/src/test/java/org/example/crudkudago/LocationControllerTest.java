package org.example.crudkudago;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.crudkudago.controller.LocationControllerImpl;
import org.example.crudkudago.entity.Location;
import org.example.crudkudago.service.LocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationControllerImpl.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationServiceImpl locationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Location location;
    private UUID locationId;

    @BeforeEach
    void setUp() {
        locationId = UUID.randomUUID();
        location = new Location();
        location.setId(locationId);
        location.setName("Test Location");
    }

    @Test
    void shouldReturnAllLocations() throws Exception {
        when(locationService.getAllLocations()).thenReturn(Collections.singletonList(location));

        var mvcResult = mockMvc.perform(get("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Location"))
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var locations = objectMapper.readValue(jsonResponse, Location[].class);
        assertThat(locations).hasSize(1);
        assertThat(locations[0].getName()).isEqualTo("Test Location");

        verify(locationService, times(1)).getAllLocations();
    }

    @Test
    void shouldReturnLocationById() throws Exception {
        when(locationService.getLocationById(locationId)).thenReturn(location);

        var mvcResult = mockMvc.perform(get("/api/v1/locations/{id}", locationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var responseLocation = objectMapper.readValue(jsonResponse, Location.class);
        assertThat(responseLocation.getName()).isEqualTo("Test Location");
        assertThat(responseLocation.getId()).isEqualTo(locationId);

        verify(locationService, times(1)).getLocationById(locationId);
    }

    @Test
    void shouldCreateLocation() throws Exception {
        var newLocation = new Location();
        newLocation.setName("New Location");
        var locationJson = objectMapper.writeValueAsString(newLocation);

        doNothing().when(locationService).createLocation(any(Location.class));

        var mvcResult = mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isOk())
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResponse).isEmpty();

        verify(locationService, times(1)).createLocation(any(Location.class));
    }

    @Test
    void shouldUpdateLocation() throws Exception {
        var updatedLocation = new Location();
        updatedLocation.setName("Updated Location");
        var locationJson = objectMapper.writeValueAsString(updatedLocation);

        doNothing().when(locationService).updateLocation(any(UUID.class), any(Location.class));

        var mvcResult = mockMvc.perform(put("/api/v1/locations/{id}", locationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isOk())
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResponse).isEmpty();

        verify(locationService, times(1)).updateLocation(any(UUID.class), any(Location.class));
    }

    @Test
    void shouldDeleteLocation() throws Exception {
        doNothing().when(locationService).deleteLocation(locationId);

        var mvcResult = mockMvc.perform(delete("/api/v1/locations/{id}", locationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResponse).isEmpty();

        verify(locationService, times(1)).deleteLocation(locationId);
    }
}
