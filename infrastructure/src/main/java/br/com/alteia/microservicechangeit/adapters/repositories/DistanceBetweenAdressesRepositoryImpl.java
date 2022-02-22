package br.com.alteia.microservicechangeit.adapters.repositories;

import br.com.alteia.microservicechangeit.entities.AddressDistance;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.IntegrationException;
import br.com.alteia.microservicechangeit.use_cases.distance.DistanceBetweenAdressesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static br.com.alteia.microservicechangeit.adapters.repositories.enums.DistanceBetweenAdressesRepositoryErrorMessages.DISTANCE_BETWEEN_ADRESSES_GENERIC;
import static br.com.alteia.microservicechangeit.configuration.CacheNames.GET_DISTANCE_BETWEEN_ADDRESSES_BY_ORIGIN_CEP_AND_DESTINATION_CEP;
import static org.springframework.http.HttpStatus.OK;

@Service
public class DistanceBetweenAdressesRepositoryImpl implements DistanceBetweenAdressesRepository {

    private static final Logger LOG = Logger.getLogger(DistanceBetweenAdressesRepositoryImpl.class.getName());

    @Value("${google.api.distance.url}")
    private String googleApiDistanceUrl;

    @Value("${google.api.distance.key}")
    private String googleApiDistanceKey;

    @Override
    @Cacheable(value = GET_DISTANCE_BETWEEN_ADDRESSES_BY_ORIGIN_CEP_AND_DESTINATION_CEP, key = "{#originCep, #destinationCep}", unless = "#result == null")
    public AddressDistance get(String originCep, String destinationCep) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(googleApiDistanceUrl
                            + "?destinations="
                            + destinationCep
                            + "&origins="
                            + originCep
                            + "&key=" + googleApiDistanceKey
                    , String.class);
        } catch (HttpClientErrorException e) {
            throw getIntegrationException(googleApiDistanceUrl, e.getStatusCode().getReasonPhrase());
        }

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        List<String> destinationAddresses;
        List<String> originAddresses;
        String distance = null;
        String duration = null;

        try {
            jsonNode = objectMapper.readTree(responseBody);

            if (OK.equals(response.getStatusCode()) && OK.getReasonPhrase().equals(jsonNode.findValue("status").asText())) {

                TypeReference<List<String>> typeReference = new TypeReference<>() {
                };

                destinationAddresses = objectMapper.readValue(jsonNode.findValue("destination_addresses").toString(), typeReference);
                originAddresses = objectMapper.readValue(jsonNode.findValue("origin_addresses").toString(), typeReference);

                JsonNode elements = jsonNode.findValue("rows").get(0).findValue("elements");
                String status = elements.findValue("status").asText();

                if ("OK".equals(status)) {
                    distance = elements.findValue("distance").get("text").asText();
                    duration = elements.findValue("duration").get("text").asText();
                }
            } else {
                throw getIntegrationException(googleApiDistanceUrl, response.getStatusCode().getReasonPhrase());
            }

        } catch (JsonProcessingException e) {
            LOG.log(Level.SEVERE, "Error deserializing json from Google Distance API", e);
            throw getIntegrationException(googleApiDistanceUrl, response.getStatusCode().getReasonPhrase());
        }

        return new AddressDistance(
                destinationAddresses.stream().findFirst().orElse(null),
                originAddresses.stream().findFirst().orElse(null),
                distance,
                duration
        );

    }

    private IntegrationException getIntegrationException(String url, String status) {
        return new IntegrationException(
                DISTANCE_BETWEEN_ADRESSES_GENERIC.getCode(),
                DISTANCE_BETWEEN_ADRESSES_GENERIC.getMessage(),
                url,
                status
        );
    }
}