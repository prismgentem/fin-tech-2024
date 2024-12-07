package org.example.crudkudago.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KudaGoEventsResponse {
    @JsonProperty("is_free")
    private Boolean isFree;

    private String title;

    private String price;
}
