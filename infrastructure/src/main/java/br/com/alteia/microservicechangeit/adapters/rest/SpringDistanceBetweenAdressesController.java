package br.com.alteia.microservicechangeit.adapters.rest;

import br.com.alteia.microservicechangeit.distance_between_adresses.DistanceBetweenAdressesController;
import br.com.alteia.microservicechangeit.distance_between_adresses.DistanceBetweenAdressesEndpointsConstants;
import br.com.alteia.microservicechangeit.use_cases.distance.dto.GetDistanceBetweenAdressesResponseDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SpringDistanceBetweenAdressesController {

    private final DistanceBetweenAdressesController distanceBetweenAdressesController;

    @Autowired
    public SpringDistanceBetweenAdressesController(final DistanceBetweenAdressesController distanceBetweenAdressesController) {
        this.distanceBetweenAdressesController = distanceBetweenAdressesController;
    }

    @ApiOperation(value = DistanceBetweenAdressesEndpointsConstants.GET_DISTANCE_BETWEEN_TWO_CEPS_DESCRIPTION)
    @GetMapping(value = DistanceBetweenAdressesEndpointsConstants.GET_DISTANCE_BETWEEN_TWO_CEPS_PATH)
    public GetDistanceBetweenAdressesResponseDto getEmployeeByIdV1(@RequestParam String originCep, @RequestParam String destinationCep) {
        return distanceBetweenAdressesController.getDistanceBetweenAdressesByOriginCepAndDestinationCepV1(originCep, destinationCep);
    }
}
