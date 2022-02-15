package br.com.alteia.microservicechangeit.use_cases.distance;

import br.com.alteia.microservicechangeit.common.exceptions.InvalidEntityException;
import br.com.alteia.microservicechangeit.entities.AddressDistance;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.NotFoundException;
import br.com.alteia.microservicechangeit.use_cases.distance.dto.GetDistanceBetweenAdressesResponseDto;

import javax.inject.Named;
import java.util.Objects;

import static br.com.alteia.microservicechangeit.use_cases.common.enums.DistanceBetweenAdressesUseCaseErrorMessages.DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_INVALID_DESTINATION;
import static br.com.alteia.microservicechangeit.use_cases.common.enums.DistanceBetweenAdressesUseCaseErrorMessages.DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_INVALID_ORIGIN;
import static br.com.alteia.microservicechangeit.use_cases.common.enums.DistanceBetweenAdressesUseCaseErrorMessages.DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_NOT_FOUND;

@Named
public class GetDistanceBetweenAdresses {

    private final DistanceBetweenAdressesRepository distanceBetweenAdressesRepository;

    public GetDistanceBetweenAdresses(DistanceBetweenAdressesRepository distanceBetweenAcressesRepository) {
        this.distanceBetweenAdressesRepository = distanceBetweenAcressesRepository;
    }

    public GetDistanceBetweenAdressesResponseDto execute(String originCep, String destinationCep) {

        try {
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
        } catch (InvalidEntityException e) {
            if (e.getMessage().contains("origin")) {
                throw new NotFoundException(
                        DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_INVALID_ORIGIN.getCode(),
                        DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_INVALID_ORIGIN.getMessage(),
                        originCep
                );
            } else if (e.getMessage().contains("destination")) {
                throw new NotFoundException(
                        DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_INVALID_DESTINATION.getCode(),
                        DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_INVALID_DESTINATION.getMessage(),
                        destinationCep
                );
            }
            throw e;
        }
    }
}
