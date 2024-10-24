package org.example.crudkudago.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceResponse {
    private UUID id;

    private String name;

    private String address;

    private String city;

    private List<EventSummary> events;
}
