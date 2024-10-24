package org.example.crudkudago.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class EventRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in the format yyyy-MM-dd")
    private String date;

    @NotNull
    private UUID placeId;
}
