package org.example.crudkudago.client;

import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.model.KudagoCategoryResponse;
import org.example.crudkudago.model.KudagoLocationResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class KudagoClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CATEGORIES_API_URL = "https://kudago.com/public-api/v1.4/place-categories";
    private static final String LOCATIONS_API_URL = "https://kudago.com/public-api/v1.4/locations";

    public List<KudagoCategoryResponse> fetchCategories(){
        log.info("Fetching categories from KudaGo API...");
        KudagoCategoryResponse[] categories = restTemplate.getForObject(CATEGORIES_API_URL, KudagoCategoryResponse[].class);
        List<KudagoCategoryResponse> categoryList = Arrays.asList(categories != null ? categories : new KudagoCategoryResponse[0]);
        log.info("Fetched {} categories.", categoryList.size());
        return categoryList;
    }

    public List<KudagoLocationResponse> fetchLocations() {
        log.info("Fetching locations from KudaGo API...");
        KudagoLocationResponse[] locations = restTemplate.getForObject(LOCATIONS_API_URL, KudagoLocationResponse[].class);
        List<KudagoLocationResponse> locationList = Arrays.asList(locations != null ? locations : new KudagoLocationResponse[0]);
        log.info("Fetched {} locations.", locationList.size());
        return locationList;
    }
}
