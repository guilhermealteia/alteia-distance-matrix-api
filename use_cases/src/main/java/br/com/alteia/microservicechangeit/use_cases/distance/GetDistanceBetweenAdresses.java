package br.com.alteia.microservicechangeit.use_cases.distance;

import br.com.alteia.microservicechangeit.entities.AddressDistance;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.NotFoundException;
import br.com.alteia.microservicechangeit.use_cases.distance.dto.GetDistanceBetweenAdressesResponseDto;

import javax.inject.Named;
import java.util.Objects;

import static br.com.alteia.microservicechangeit.use_cases.common.enums.DistanceBetweenAdressesUseCaseErrorMessages.DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_NOT_FOUND;

@Named
public class GetDistanceBetweenAdresses {

    private final DistanceBetweenAdressesRepository distanceBetweenAdressesRepository;

    public GetDistanceBetweenAdresses(DistanceBetweenAdressesRepository distanceBetweenAcressesRepository) {
        this.distanceBetweenAdressesRepository = distanceBetweenAcressesRepository;
    }

    public GetDistanceBetweenAdressesResponseDto execute(String originCep, String destinationCep) {

        AddressDistance addressDistance = distanceBetweenAdressesRepository.get(originCep, destinationCep);

        if (Objects.isNull(addressDistance)) {
            throw new NotFoundException(
                    DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_NOT_FOUND.getCode(),
                    DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_NOT_FOUND.getMessage(),
                    originCep,
                    destinationCep
            );
        }
        return new GetDistanceBetweenAdressesResponseDto(addressDistance);
    }
}
