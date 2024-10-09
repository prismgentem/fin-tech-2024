package org.example.lesson8;

import org.example.lesson8.controller.response.ConvertResponse;
import org.example.lesson8.controller.response.RateResponse;
import org.example.lesson8.exception.ErrorType;
import org.example.lesson8.exception.ServiceException;
import org.example.lesson8.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Container
    private static final WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:2.35.0")
            .withMappingFromResource("wiremock/currency-rates-mappings.json");

    @DynamicPropertySource
    static void overrideCbrApiUrl(DynamicPropertyRegistry registry) {
        String wireMockUrl = String.format("http://%s:%d", wiremockServer.getHost(), wiremockServer.getMappedPort(8080));
        registry.add("cbr.api-url", () -> wireMockUrl + "/scripts/XML_daily.asp");
    }

    @BeforeEach
    void setUp() {
        when(currencyService.getRate("USD")).thenReturn(
                RateResponse.builder()
                        .currency("USD")
                        .rate(96.9483).build());

        ConvertResponse response = ConvertResponse.builder()
                .fromCurrency("USD")
                .toCurrency("RUB")
                .convertedAmount(7300.0)
                .build();

        Mockito.when(currencyService.convert(Mockito.any())).thenReturn(response);
    }

    @Test
    void shouldReturnRateForValidCurrencyCode() throws Exception {
        mockMvc.perform(get("/currencies/rates/USD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.rate").value(96.9483));
    }

    @Test
    void shouldConvertCurrencySuccessfully() throws Exception {
        String requestBody = """
                {
                    "fromCurrency": "USD",
                    "toCurrency": "RUB",
                    "amount": 100
                }
                """;

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCurrency").value("USD"))
                .andExpect(jsonPath("$.toCurrency").value("RUB"))
                .andExpect(jsonPath("$.convertedAmount").value(7300.0));
    }

    // Негативный тест: Некорректный код валюты
    @Test
    void shouldReturnBadRequestForInvalidCurrencyCode() throws Exception {
        Mockito.when(currencyService.getRate("INVALID")).thenThrow(new ServiceException(ErrorType.INVALID_REQUEST, "Currency code is invalid"));

        mockMvc.perform(get("/currencies/rates/INVALID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Currency code is invalid"));
    }

    // Негативный тест: amount <= 0
    @Test
    void shouldReturnBadRequestForNegativeAmount() throws Exception {
        String requestBody = """
                {
                    "fromCurrency": "USD",
                    "toCurrency": "RUB",
                    "amount": -100
                }
                """;

        Mockito.when(currencyService.convert(Mockito.any())).thenThrow(new ServiceException(ErrorType.INVALID_REQUEST, "Amount must be greater than zero"));

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Amount must be greater than zero"));
    }

    // Негативный тест: Недоступность API ЦБ
    @Test
    void shouldReturnServiceUnavailableWhenCbrApiFails() throws Exception {
        Mockito.when(currencyService.getRate("USD"))
                .thenThrow(new ServiceException(ErrorType.SERVICE_UNAVAILABLE, "CBR API is unavailable"));

        mockMvc.perform(get("/currencies/rates/USD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("SERVICE_UNAVAILABLE"))
                .andExpect(jsonPath("$.message").value("CBR API is unavailable"));
    }
}
