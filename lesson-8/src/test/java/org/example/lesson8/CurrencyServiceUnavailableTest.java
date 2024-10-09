package org.example.lesson8;

import org.example.lesson8.client.CbrClient;
import org.example.lesson8.exception.ServiceException;
import org.example.lesson8.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CurrencyServiceUnavailableTest {

    @Mock
    private CbrClient cbrClient;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        when(cbrClient.getCurrencyRates()).thenReturn(Mono.empty());
    }

    @Test
    void testServiceUnavailable() {
        assertThrows(ServiceException.class, () -> {
            currencyService.getRate("USD");
        });
    }
}

