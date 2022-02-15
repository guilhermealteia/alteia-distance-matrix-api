package br.com.alteia.microservicechangeit.distance_between_adresses;

import br.com.alteia.microservicechangeit.use_cases.distance.GetDistanceBetweenAdresses;
import br.com.alteia.microservicechangeit.use_cases.distance.dto.GetDistanceBetweenAdressesResponseDto;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class DistanceBetweenAdressesController {

    @Inject
    private GetDistanceBetweenAdresses getDistanceBetweenAdresses;

    public GetDistanceBetweenAdressesResponseDto getDistanceBetweenAdressesByOriginCepAndDestinationCepV1(String originCep, String destinationCep) {
        return getDistanceBetweenAdresses.execute(originCep, destinationCep);
    }
}
