package org.example.crudkudago.model.snapshot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySnapshot {
    private UUID id;
    private String slug;
    private String name;
}
