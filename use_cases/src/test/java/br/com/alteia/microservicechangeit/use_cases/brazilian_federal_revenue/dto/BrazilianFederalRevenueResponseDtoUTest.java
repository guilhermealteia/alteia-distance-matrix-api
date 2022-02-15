package br.com.alteia.microservicechangeit.use_cases.brazilian_federal_revenue.dto;

import br.com.alteia.microservicechangeit.entities.BrazilianFederalRevenueData;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BrazilianFederalRevenueResponseDtoUTest {

    @Nested
    class ToBrazilianFederalRevenueDataShould {

        @Test
        void return_new_dto_object_with_entity_value() {
            // Given
            BrazilianFederalRevenueData revenueData = new BrazilianFederalRevenueData("Julia Neves", false, LocalDate.parse("1990-01-01"));

            // When
            BrazilianFederalRevenueResponseDto brazilianFederalRevenueResponseDto = new BrazilianFederalRevenueResponseDto(revenueData);

            // Then
            assertEquals(revenueData.getDateOfLastDocumentUpdate().toString(), brazilianFederalRevenueResponseDto.getDateOfLastDocumentUpdate());
            assertEquals(revenueData.getMotherName(), brazilianFederalRevenueResponseDto.getMotherName());
        }
    }

    @Test
    void validate_class_have_required_methods() {
        String id = UUID.randomUUID().toString();

        BrazilianFederalRevenueResponseDto federalRevenueResponseDto1 = new BrazilianFederalRevenueResponseDto();
        federalRevenueResponseDto1.setMotherName("Julia Neves");
        federalRevenueResponseDto1.setDateOfLastDocumentUpdate("1990-01-01");

        BrazilianFederalRevenueResponseDto federalRevenueResponseDto2 = new BrazilianFederalRevenueResponseDto();
        federalRevenueResponseDto2.setMotherName(federalRevenueResponseDto1.getMotherName());
        federalRevenueResponseDto2.setDateOfLastDocumentUpdate(federalRevenueResponseDto1.getDateOfLastDocumentUpdate());

        assertDoesNotThrow(() -> new BrazilianFederalRevenueResponseDto(null));

        EqualsVerifier.simple().forClass(BrazilianFederalRevenueResponseDto.class).verify();

        int hashCode1 = federalRevenueResponseDto1.hashCode();
        int hashCode2 = federalRevenueResponseDto2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

}