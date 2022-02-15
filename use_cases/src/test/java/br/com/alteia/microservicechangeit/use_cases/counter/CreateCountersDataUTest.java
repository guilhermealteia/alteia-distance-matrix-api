package br.com.alteia.microservicechangeit.use_cases.counter;

import br.com.alteia.microservicechangeit.use_cases.counter.dto.CreateCounterRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static br.com.alteia.microservicechangeit.test_utils.CounterGenerator.mockCounter;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class CreateCountersDataUTest {

    private CreateCountersData createCountersData;

    @Mock
    private CounterBatchRepository counterBatchRepository;

    @BeforeEach
    void setUp() {
        createCountersData = new CreateCountersData(counterBatchRepository);
    }

    @Test
    void create_counter_data() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();

        CreateCounterRequestDto counter = new CreateCounterRequestDto(mockCounter(id));

        // Quando // Então
        assertDoesNotThrow(() -> createCountersData.execute(List.of(counter)));
    }
}