package org.example.crudkudago.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.exception.ErrorType;
import org.example.crudkudago.exception.ServiceException;
import org.example.crudkudago.model.KudaGoEventsResponse;
import org.example.crudkudago.model.KudagoCategoryResponse;
import org.example.crudkudago.model.KudagoLocationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class KudagoClient {
    private final ObjectMapper objectMapper;
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

    public List<KudaGoEventsResponse> getEvents(LocalDate dateFrom, LocalDate dateTo) {
        String url = baseUrl + "/events" + addParams(dateFrom, dateTo);

        try {
            var jsonResponse = restTemplate.getForObject(url, String.class);

            var rootNode = objectMapper.readTree(jsonResponse);
            var resultsNode = rootNode.get("results");

            KudaGoEventsResponse[] events = objectMapper.readerFor(KudaGoEventsResponse[].class).readValue(resultsNode);
            return Arrays.asList(events);

        } catch (Exception e) {
            throw new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, MSG_CLIENT_ERROR, e);
        }
    }

    private String addParams(LocalDate dateFrom, LocalDate dateTo) {
        long actualSince = convertLocalDateToUnixTimestamp(dateFrom);
        long actualUntil = convertLocalDateToUnixTimestamp(dateTo);
        return String.format("/?fields=title,price,is_free&actual_since=%s&actual_until=%s&order_by=-favorites_count",
                actualSince, actualUntil);
    }

    private long convertLocalDateToUnixTimestamp(LocalDate date) {
        return date.atStartOfDay(ZoneId.of("UTC")).toEpochSecond();
    }
}
