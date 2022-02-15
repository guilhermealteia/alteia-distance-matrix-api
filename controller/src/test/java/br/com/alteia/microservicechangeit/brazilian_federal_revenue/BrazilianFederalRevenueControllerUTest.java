package br.com.alteia.microservicechangeit.brazilian_federal_revenue;

import br.com.alteia.microservicechangeit.entities.BrazilianFederalRevenueData;
import br.com.alteia.microservicechangeit.use_cases.brazilian_federal_revenue.GetBrazilianFederalRevenueData;
import br.com.alteia.microservicechangeit.use_cases.brazilian_federal_revenue.dto.BrazilianFederalRevenueResponseDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrazilianFederalRevenueControllerUTest {

    @InjectMocks
    private BrazilianFederalRevenueController brazilianFederalRevenueController;

    @Mock
    private GetBrazilianFederalRevenueData getBrazilianFederalRevenueData;

    @Nested
    class GetBrazilianFederalRevenueDataByNameV1Should {

        @Test
        void return_brazilian_federal_revenue_data() {
            // Given
            String name = "Josiclayton Perera";
            BrazilianFederalRevenueData revenueData = new BrazilianFederalRevenueData("Jacuzi Perera", false, LocalDate.parse("1994-06-01"));

            when(getBrazilianFederalRevenueData.execute(name)).thenReturn(new BrazilianFederalRevenueResponseDto(revenueData));

            // When
            BrazilianFederalRevenueResponseDto result = brazilianFederalRevenueController.getBrazilianFederalRevenueDataByNameV1(name);

            // Then
            assertEquals(result, new BrazilianFederalRevenueResponseDto(revenueData));
        }
    }
}