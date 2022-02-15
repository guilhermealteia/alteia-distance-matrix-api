package br.com.alteia.microservicechangeit.use_cases.counter;

import br.com.alteia.microservicechangeit.entities.Counter;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.NotFoundException;
import br.com.alteia.microservicechangeit.use_cases.counter.dto.GetCounterResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUniqueCounterDataUTest {

    private GetUniqueCounterData getUniqueCounterData;

    @Mock
    private CounterRepository counterRepository;

    @BeforeEach
    void setUp() {
        getUniqueCounterData = new GetUniqueCounterData(counterRepository);
    }

    @Test
    void return_unique_counter_data() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        Counter counter = new Counter(id, "Contador de Exemplo", 1230);

        when(counterRepository.findCounterById(id)).thenReturn(counter);

        // Quando
        GetCounterResponseDto result = getUniqueCounterData.execute(id);

        // Então
        assertEquals(new GetCounterResponseDto(counter), result);
    }

    @Test
    void should_throw_exception_get_counter_data_that_not_exists() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        Counter counter = new Counter(id, "Contador de Teste", 2450);

        String errorMessage = null;

        // Quando
        try {
            getUniqueCounterData.execute(id);
        } catch (NotFoundException ex) {
            errorMessage = ex.getMessage();
        }

        // Então
        assertEquals(String.format("O contador com id %s não existe", id), errorMessage);
    }
}