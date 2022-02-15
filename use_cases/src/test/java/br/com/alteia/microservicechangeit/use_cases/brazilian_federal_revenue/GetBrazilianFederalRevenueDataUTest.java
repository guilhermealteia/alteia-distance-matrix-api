package br.com.alteia.microservicechangeit.use_cases.brazilian_federal_revenue;

import br.com.alteia.microservicechangeit.entities.BrazilianFederalRevenueData;
import br.com.alteia.microservicechangeit.use_cases.brazilian_federal_revenue.dto.BrazilianFederalRevenueResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBrazilianFederalRevenueDataUTest {

    private GetBrazilianFederalRevenueData getBrazilianFederalRevenueData;

    @Mock
    private BrazilianFederalRevenueDataRepository brazilianFederalRevenueDataRepository;

    @BeforeEach
    void setUp() {
        getBrazilianFederalRevenueData = new GetBrazilianFederalRevenueData(brazilianFederalRevenueDataRepository);
    }

    @Test
    void return_brazilian_federal_revenue_data() {
        // Dados os elementos abaixo
        String customerName = "Guilherme Alteia";
        BrazilianFederalRevenueData revenueData = new BrazilianFederalRevenueData("Mão do Guilherme", false, LocalDate.parse("1994-06-01"));

        when(brazilianFederalRevenueDataRepository.findBrazilianFederalRevenueDataByName(customerName)).thenReturn(revenueData);

        // Quando
        BrazilianFederalRevenueResponseDto result = getBrazilianFederalRevenueData.execute(customerName);

        // Então
        assertEquals(new BrazilianFederalRevenueResponseDto(revenueData), result);
    }
}