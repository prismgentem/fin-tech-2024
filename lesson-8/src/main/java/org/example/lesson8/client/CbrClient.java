package org.example.lesson8.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CbrClient {

    private final WebClient webClient;

    @CircuitBreaker(name = "cbrService")
    public Mono<String> getCurrencyRates() {
        return webClient.get()
                .retrieve()
                .bodyToMono(String.class);
    }
}
