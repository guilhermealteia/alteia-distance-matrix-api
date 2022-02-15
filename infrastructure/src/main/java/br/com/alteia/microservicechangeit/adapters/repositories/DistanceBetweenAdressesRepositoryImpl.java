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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static br.com.alteia.microservicechangeit.configuration.CacheNames.GET_DISTANCE_BETWEEN_ADDRESSES_BY_ORIGIN_CEP_AND_DESTINATION_CEP;
import static br.com.alteia.microservicechangeit.use_cases.common.enums.DistanceBetweenAdressesUseCaseErrorMessages.DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_GENERIC;
import static org.springframework.http.HttpStatus.OK;

@Service
public class DistanceBetweenAdressesRepositoryImpl implements DistanceBetweenAdressesRepository {

    private static final Logger LOG = Logger.getLogger(DistanceBetweenAdressesRepositoryImpl.class.getName());

    private final String ADDRESS_NOT_FOUND = "NOT_FOUND";

    @Value("${google.api.distance.url}")
    private String googleApiDistanceUrl;

    @Value("${google.api.distance.key}")
    private String googleApiDistanceKey;

    @Override
    @Cacheable(value = GET_DISTANCE_BETWEEN_ADDRESSES_BY_ORIGIN_CEP_AND_DESTINATION_CEP, key = "{#originCep, #destinationCep}", unless = "#result == null")
    public AddressDistance get(String originCep, String destinationCep) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(googleApiDistanceUrl
                        + "?destinations="
                        + destinationCep
                        + "&origins="
                        + originCep
                        + "&key=" + googleApiDistanceKey
                , String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        List<String> destinationAddresses = null;
        List<String> originAddresses = null;
        String distance = null;
        String duration = null;

        try {
            jsonNode = objectMapper.readTree(responseBody);

            if (OK.equals(response.getStatusCode()) && OK.getReasonPhrase().equals(jsonNode.findValue("status").asText())) {

                destinationAddresses = objectMapper.readValue(jsonNode.findValue("destination_addresses").toString(), new TypeReference<List<String>>() {
                });
                originAddresses = objectMapper.readValue(jsonNode.findValue("origin_addresses").toString(), new TypeReference<List<String>>() {
                });

                JsonNode rows = jsonNode.findValue("rows");
                JsonNode elements = rows.get(0).findValue("elements");
                String status = elements.findValue("status").asText();

                if (!ADDRESS_NOT_FOUND.equals(status)) {
                    distance = elements.findValue("distance").get("text").asText();
                    duration = elements.findValue("duration").get("text").asText();
                }

            } else {
                throw new IntegrationException(
                        DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_GENERIC.getCode(),
                        DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_GENERIC.getMessage(),
                        response.getStatusCode(),
                        response.getBody()
                );
            }

        } catch (JsonProcessingException e) {
            LOG.log(Level.SEVERE, "Error deserializing json from Google Distance API", e);
            return null;
        }

        return new AddressDistance(
                destinationAddresses.stream().findFirst().orElse(null),
                originAddresses.stream().findFirst().orElse(null),
                distance,
                duration
        );

    }
}