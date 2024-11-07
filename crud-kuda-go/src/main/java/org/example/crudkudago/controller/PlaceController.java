package org.example.crudkudago.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crudkudago.model.PlaceRequest;
import org.example.crudkudago.model.PlaceResponse;
import org.example.crudkudago.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/places")
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<PlaceResponse>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponse> getPlaceById(@PathVariable UUID id) {
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }

    @PostMapping
    public ResponseEntity<PlaceResponse> createPlace(@Valid @RequestBody PlaceRequest request) {
        return ResponseEntity.ok(placeService.createPlace(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceResponse> updatePlace(@PathVariable UUID id, @Valid @RequestBody PlaceRequest request) {
        return ResponseEntity.ok(placeService.updatePlace(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable UUID id) {
        placeService.deletePlaceById(id);
        return ResponseEntity.ok().build();
    }
}