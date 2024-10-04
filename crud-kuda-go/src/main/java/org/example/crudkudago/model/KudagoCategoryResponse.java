package org.example.crudkudago.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KudagoCategoryResponse {
    private Integer id;
    private String slug;
    private String name;
}
