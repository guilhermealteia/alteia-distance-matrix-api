package br.com.alteia.microservicechangeit.use_cases.counter;

import br.com.alteia.microservicechangeit.entities.Counter;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.MismatchException;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.NotFoundException;
import br.com.alteia.microservicechangeit.use_cases.counter.dto.UpdateCounterRequestDto;
import br.com.alteia.microservicechangeit.use_cases.counter.dto.UpdateCounterResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static br.com.alteia.microservicechangeit.test_utils.CounterGenerator.mockCounter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCounterDataUTest {

    private UpdateCounterData updateCounterData;

    @Mock
    private CounterRepository counterRepository;

    @BeforeEach
    void setUp() {
        updateCounterData = new UpdateCounterData(counterRepository);
    }

    @Test
    void update_counter_data_without_changes() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        UpdateCounterRequestDto counter = new UpdateCounterRequestDto(mockCounter(id));

        when(counterRepository.findCounterById(id)).thenReturn(mockCounter(id));

        // Quando
        UpdateCounterResponseDto result = updateCounterData.execute(id, counter);

        assertNotNull(result);

        // Então
        verify(counterRepository).findCounterById(id);
        //Importante essa validação para garantir que
        // o método que verifica a necessidade do update realmente funcionou
        verifyNoMoreInteractions(counterRepository);
    }

    @Test
    void update_and_return_updated_counter_data() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        Counter counter = mockCounter(id);
        Counter updatedCounter = mockCounter(id, "Nome de Contador Alterado", 1000);

        when(counterRepository.findCounterById(id)).thenReturn(counter);
        when(counterRepository.save(any(Counter.class))).thenReturn(updatedCounter);

        // Quando
        UpdateCounterResponseDto result = updateCounterData.execute(id, new UpdateCounterRequestDto(updatedCounter));

        // Então
        assertEquals(result.getName(), updatedCounter.getName());
        assertEquals(result.getValue(), updatedCounter.getValue());
    }

    @Test
    void should_throw_exception_update_counter_data_with_different_ids() {
        // Dados os elementos abaixo
        String id1 = UUID.randomUUID().toString();
        Counter counter = new Counter(id1, "Contador de Testes 1", 2032);

        String errorMessage = null;

        String id2 = UUID.randomUUID().toString();
        // Quando
        try {
            updateCounterData.execute(id2, new UpdateCounterRequestDto(counter));
        } catch (MismatchException ex) {
            errorMessage = ex.getMessage();
        }

        // Então
        assertEquals(String.format("O id informado para a atualização: %s difere do recebido no objeto a atualizar: %s", id2, id1), errorMessage);
    }

    @Test
    void should_throw_exception_update_counter_data_that_not_exists() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        Counter counter = new Counter(id, "Contador de Testes 1", 2032);

        String errorMessage = null;

        // Quando
        try {
            updateCounterData.execute(id, new UpdateCounterRequestDto(counter));
        } catch (NotFoundException ex) {
            errorMessage = ex.getMessage();
        }

        // Então
        assertEquals(String.format("O contador com id %s não existe", id), errorMessage);
    }

}