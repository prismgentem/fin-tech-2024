package org.example.crudkudago.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.exception.ErrorType;
import org.example.crudkudago.exception.ServiceException;
import org.example.crudkudago.model.KudaGoEventsResponse;
import org.example.crudkudago.model.KudagoCategoryResponse;
import org.example.crudkudago.model.KudagoLocationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

@Component
@Slf4j
@RequiredArgsConstructor
public class KudagoClient {
    private final ObjectMapper objectMapper;
    private WebClient webClient;
    private Semaphore rateLimiter;

    private static final String MSG_CLIENT_ERROR = "Error with kudago api request '%s'";

    @Value("${external.api.kudago.base-url}")
    private String baseUrl;

    @Value("${app.rate-limit.max-concurrent-requests}")
    private int maxConcurrentRequests;

    @PostConstruct
    public void init() {
        rateLimiter = new Semaphore(maxConcurrentRequests);

        HttpClient httpClient = HttpClient.create()
                .followRedirect(true);

        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public Mono<List<KudagoCategoryResponse>> fetchCategories() {
        return Mono.fromCallable(() -> {
            rateLimiter.acquire();
            return null;
        }).flatMap(ignored -> {
            var url = baseUrl + "/place-categories";
            log.info("Fetching categories from KudaGo API...");

            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(KudagoCategoryResponse[].class)
                    .map(Arrays::asList)
                    .doOnSuccess(categories -> log.info("Fetched {} categories.", categories.size()))
                    .doOnError(e -> log.error("Error fetching categories: {}", e.getMessage()))
                    .onErrorMap(e -> new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, MSG_CLIENT_ERROR, e))
                    .doFinally(signal -> rateLimiter.release());
        });
    }

    public Mono<List<KudagoLocationResponse>> fetchLocations() {
        return Mono.fromCallable(() -> {
            rateLimiter.acquire();
            return null;
        }).flatMap(ignored -> {
            var url = baseUrl + "/locations";
            log.info("Fetching locations from KudaGo API...");

            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(KudagoLocationResponse[].class)
                    .map(Arrays::asList)
                    .doOnSuccess(locations -> log.info("Fetched {} locations.", locations.size()))
                    .doOnError(e -> log.error("Error fetching locations: {}", e.getMessage()))
                    .onErrorMap(e -> new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, MSG_CLIENT_ERROR, e))
                    .doFinally(signal -> rateLimiter.release());
        });
    }

    public Flux<KudaGoEventsResponse> getEvents(LocalDate dateFrom, LocalDate dateTo) {
        return Flux.defer(() -> {
            try {
                rateLimiter.acquire();
                String url = baseUrl + "/events" + addParams(dateFrom, dateTo);
                log.info("Fetching events from KudaGo API...");

                return webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(String.class)
                        .flatMapMany(jsonResponse -> {
                            try {
                                var rootNode = objectMapper.readTree(jsonResponse);
                                var resultsNode = rootNode.get("results");

                                KudaGoEventsResponse[] events = objectMapper
                                        .readerFor(KudaGoEventsResponse[].class)
                                        .readValue(resultsNode);

                                return Flux.fromArray(events);
                            } catch (Exception e) {
                                log.error("Error parsing events response: {}", e.getMessage());
                                return Flux.error(new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, MSG_CLIENT_ERROR, e));
                            }
                        })
                        .doFinally(signal -> rateLimiter.release());
            } catch (InterruptedException e) {
                log.error("Semaphore acquire interrupted", e);
                return Flux.error(new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, "Semaphore acquire interrupted", e));
            }
        });
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
