package org.example.crudkudago.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KudagoLocationResponse {
    private String slug;
    private String name;
}
