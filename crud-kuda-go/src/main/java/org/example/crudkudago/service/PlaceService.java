package org.example.crudkudago.service;

import lombok.RequiredArgsConstructor;
import org.example.crudkudago.entity.Place;
import org.example.crudkudago.exception.ErrorType;
import org.example.crudkudago.exception.ServiceException;
import org.example.crudkudago.model.PlaceRequest;
import org.example.crudkudago.model.PlaceResponse;
import org.example.crudkudago.repository.PlaceRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceService {
    public static final String MSG_PLACE_EXIST = "Place with name %s, address %s, city %s already exist.";
    public static final String MSG_PLACE_NOT_FOUND = "Place with id %s not found.";

    private final PlaceRepository placeRepository;
    private final ConversionService conversionService;

    @Transactional(readOnly = true)
    public PlaceResponse getPlaceById(UUID id) {
        var place = validateAndGetPlace(id);
        return conversionService.convert(place, PlaceResponse.class);
    }

    @Transactional(readOnly = true)
    public List<PlaceResponse> getAllPlaces() {
        var places = placeRepository.findAll();
        return places.stream()
                .map(place -> conversionService.convert(place, PlaceResponse.class))
                .toList();
    }

    @Transactional
    public PlaceResponse createPlace(PlaceRequest placeRequest) {
        if (placeRepository.findByNameAndAddressAndCity(placeRequest.getName(), placeRequest.getAddress(), placeRequest.getCity()).isPresent()) {
            throw new ServiceException(ErrorType.CONFLICT, MSG_PLACE_EXIST, placeRequest.getName(), placeRequest.getAddress(), placeRequest.getCity());
        }

        var place = conversionService.convert(placeRequest, Place.class);

        return conversionService.convert(placeRepository.save(place), PlaceResponse.class);
    }

    @Transactional
    public PlaceResponse updatePlace(PlaceRequest placeRequest, UUID id) {
        var place = validateAndGetPlace(id);
        var updatedPlace = conversionService.convert(placeRequest, Place.class);
        place.setName(updatedPlace.getName());
        place.setAddress(updatedPlace.getAddress());
        place.setCity(updatedPlace.getCity());
        return conversionService.convert(placeRepository.save(place), PlaceResponse.class);
    }

    @Transactional
    public void deletePlaceById(UUID id) {
        var place = validateAndGetPlace(id);
        placeRepository.delete(place);
    }

    private Place validateAndGetPlace(UUID id) {
        var place = placeRepository.findById(id);
        if (place.isEmpty()) {
            throw new ServiceException(ErrorType.NOT_FOUND, MSG_PLACE_NOT_FOUND, id);
        }
        return place.get();
    }
}
