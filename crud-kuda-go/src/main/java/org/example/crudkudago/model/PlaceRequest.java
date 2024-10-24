package org.example.crudkudago.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String city;
}
