package br.com.alteia.microservicechangeit.use_cases.distance.dto;

import br.com.alteia.microservicechangeit.entities.AddressDistance;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;


public class GetDistanceBetweenAdressesResponseDto {

    @ApiModelProperty(value = "CEP origem", required = true)
    @JsonProperty(required = true)
    private String origin;

    @ApiModelProperty(value = "CEP destino", required = true)
    @JsonProperty(required = true)
    private String destination;

    @ApiModelProperty(value = "Distância dentre origem e destino", required = true)
    @JsonProperty(required = true)
    private String distance;

    @ApiModelProperty(value = "Duração do trajeto", required = true)
    @JsonProperty(required = true)
    private String duration;

    public GetDistanceBetweenAdressesResponseDto() {
    }

    public GetDistanceBetweenAdressesResponseDto(AddressDistance addressDistance) {
        if (Objects.nonNull(addressDistance)) {
            this.origin = addressDistance.getOrigin();
            this.destination = addressDistance.getDestination();
            this.distance = addressDistance.getDistance();
            this.duration = addressDistance.getDuration();
        }
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
