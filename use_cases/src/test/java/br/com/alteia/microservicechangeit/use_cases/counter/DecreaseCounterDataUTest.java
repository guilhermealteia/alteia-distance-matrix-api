package br.com.alteia.microservicechangeit.use_cases.counter;

import br.com.alteia.microservicechangeit.entities.Counter;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.InvalidRequestException;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecreaseCounterDataUTest {

    private DecreaseCounter decreaseCounter;

    @Mock
    private CounterRepository counterRepository;

    @BeforeEach
    void setUp() {
        decreaseCounter = new DecreaseCounter(counterRepository);
    }

    @Test
    void update_counter_data_without_affected_rows_and_not_existent_counter() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();

        // Quando
        when(counterRepository.decreaseCounter(id)).thenReturn(0);
        when(counterRepository.findCounterById(id)).thenReturn(null);

        // Então
        assertThrows(NotFoundException.class, () -> decreaseCounter.execute(id));
    }

    @Test
    void update_counter_data_without_affected_rows_and_existent_counter() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        Counter counter = new Counter(id, "Contador 1", 1000);

        // Quando
        when(counterRepository.decreaseCounter(id)).thenReturn(0);
        when(counterRepository.findCounterById(id)).thenReturn(counter);

        // Então
        assertThrows(InvalidRequestException.class, () -> decreaseCounter.execute(id));
    }

    @Test
    void update_counter_data_with_affected_rows() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        Counter counter = new Counter(id, "Contador 1", 1000);

        // Quando
        when(counterRepository.decreaseCounter(id)).thenReturn(1);

        // Então
        assertDoesNotThrow(() -> decreaseCounter.execute(id));
    }

    @Test
    void should_throw_exception_update_counter_data_that_not_exists() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();

        String errorMessage = null;

        // Quando
        try {
            decreaseCounter.execute(id);
        } catch (NotFoundException ex) {
            errorMessage = ex.getMessage();
        }

        // Então
        assertEquals(String.format("O contador com id %s não existe", id), errorMessage);
    }

}