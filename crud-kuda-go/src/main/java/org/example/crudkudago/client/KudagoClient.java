package org.example.crudkudago.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.exception.ErrorType;
import org.example.crudkudago.exception.ServiceException;
import org.example.crudkudago.model.KudagoCategoryResponse;
import org.example.crudkudago.model.KudagoLocationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class KudagoClient {

    private final RestTemplate restTemplate;

    private static final String MSG_CLIENT_ERROR = "Error with kudago api request '%s'";

    @Value("${external.api.kudago.base-url}")
    private String baseUrl;

    public List<KudagoCategoryResponse> fetchCategories(){
        try {
            var url = baseUrl + "/place-categories";
            log.info("Fetching categories from KudaGo API...");
            KudagoCategoryResponse[] categories = restTemplate.getForObject(url, KudagoCategoryResponse[].class);
            List<KudagoCategoryResponse> categoryList = Arrays.asList(categories != null ? categories : new KudagoCategoryResponse[0]);
            log.info("Fetched {} categories.", categoryList.size());
            return categoryList;
        } catch (RestClientException e) {
            throw new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, MSG_CLIENT_ERROR, e);
        }
    }

    public List<KudagoLocationResponse> fetchLocations() {
        try {
            var url = baseUrl + "/locations";
            log.info("Fetching locations from KudaGo API...");
            KudagoLocationResponse[] locations = restTemplate.getForObject(url, KudagoLocationResponse[].class);
            List<KudagoLocationResponse> locationList = Arrays.asList(locations != null ? locations : new KudagoLocationResponse[0]);
            log.info("Fetched {} locations.", locationList.size());
            return locationList;
        } catch (RestClientException e) {
            throw new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, MSG_CLIENT_ERROR, e);
        }
    }
}
