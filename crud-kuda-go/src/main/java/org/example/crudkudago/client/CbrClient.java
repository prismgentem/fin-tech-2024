package org.example.crudkudago.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CbrClient {

    private final WebClient webClient;

    @Value("${external.api.cbr.base-url}")
    private String cbrUrl;

    public Mono<String> getCurrencyRates() {
        return webClient.get()
                .uri(cbrUrl)
                .retrieve()
                .bodyToMono(String.class);
    }
}