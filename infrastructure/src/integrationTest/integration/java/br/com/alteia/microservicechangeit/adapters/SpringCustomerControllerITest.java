package br.com.alteia.microservicechangeit.adapters;

import br.com.alteia.microservicechangeit.CleanArchitectureChangeItApplication;
import br.com.alteia.microservicechangeit.adapters.repositories.CustomerMongoRepository;
import br.com.alteia.microservicechangeit.adapters.rest.SpringCustomerController;
import br.com.alteia.microservicechangeit.customer.CustomerController;
import br.com.alteia.microservicechangeit.entities.customer.Customer;
import br.com.alteia.microservicechangeit.use_cases.customer.CreateCustomerData;
import br.com.alteia.microservicechangeit.use_cases.customer.GetAllCustomerData;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.CreateCustomerRequestDto;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.GetCustomerResponseDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static br.com.alteia.common.tests.TestUtils.fromObjectToJson;
import static br.com.alteia.microservicechangeit.customer.CustomerEndpointsConstants.CREATE_CUSTOMER_DATA_V1_PATH;
import static br.com.alteia.microservicechangeit.customer.CustomerEndpointsConstants.GET_ALL_CUSTOMER_V1_PATH;
import static br.com.alteia.microservicechangeit.test_utils.CustomerGenerator.CPF;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(classes = {CleanArchitectureChangeItApplication.class})
@AutoConfigureMockMvc
public class SpringCustomerControllerITest {

    @Autowired
    private SpringCustomerController springCustomerController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerController customerController;

    @MockBean
    private GetAllCustomerData getAllCustomerData;

    @Autowired
    private CreateCustomerData createCustomerData;

    @Autowired
    private CustomerMongoRepository customerRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Container
    private static final DockerComposeContainer<?> dockerComposeContainer = new DockerComposeContainer<>(new File("docker-compose-test.yaml"))
            .withLocalCompose(true);

    @PostConstruct
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(springCustomerController);
    }

    @Nested
    class GetOnApiCustomerEndpointShould {
        @Test
        void return_customer_data_as_json_array() throws Exception {
            // Given
            String id1 = UUID.randomUUID().toString();
            Customer customer1 = new Customer(id1, "Guilherme Alteia", LocalDate.now(), "12007519062");

            String id2 = UUID.randomUUID().toString();
            Customer customer2 = new Customer(id2, "Jose Pereira Silva", LocalDate.now(), "46370879029");

            //When
            when(getAllCustomerData.execute()).thenReturn(
                    List.of(
                            new GetCustomerResponseDto(customer1),
                            new GetCustomerResponseDto(customer2)
                    )
            );

            //Then
            String response = mockMvc.perform(get(GET_ALL_CUSTOMER_V1_PATH))
                    .andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

            GetCustomerResponseDto[] getCustomerResponseDtos = new Gson().fromJson(response, GetCustomerResponseDto[].class);
            assertEquals(new GetCustomerResponseDto(customer1), getCustomerResponseDtos[0]);
            assertEquals(new GetCustomerResponseDto(customer2), getCustomerResponseDtos[1]);
        }
    }

    @Nested
    class PostOnApiCustomerEndpointShould {
        @Test
        void return_created_customer_data_as_json_object() throws Exception {
            // Given
            CreateCustomerRequestDto createCustomerRequestDto = new CreateCustomerRequestDto(
                    "Joselino Silva",
                    "2000-09-01",
                    "71853106011"
            );

            // Then
            mockMvc.perform(post(CREATE_CUSTOMER_DATA_V1_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(fromObjectToJson(createCustomerRequestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString("{\"id\":")));
        }

        @Test
        void return_bad_request_when_customer_name_is_invalid() throws Exception {
            // Given
            CreateCustomerRequestDto createCustomerRequestDto = new CreateCustomerRequestDto("", "2021-01-01", CPF);

            // When// Then
            mockMvc.perform(post(CREATE_CUSTOMER_DATA_V1_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(fromObjectToJson(createCustomerRequestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("{\"httpStatus\":\"BAD_REQUEST\",\"code\":\"FLDVLDTNS0002\",\"details\":[\"O tamanho do campo name deve ser entre 1 e 100\"]}")));
        }

        @Test
        void return_bad_request_when_customer__birthday_is_invalid() throws Exception {
            // Given
            CreateCustomerRequestDto createCustomerRequestDto = new CreateCustomerRequestDto("Afonso Silva", "", CPF);

            // When// Then
            mockMvc.perform(post(CREATE_CUSTOMER_DATA_V1_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(fromObjectToJson(createCustomerRequestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(
                            "{\"httpStatus\":\"BAD_REQUEST\",\"code\":\"FLDVLDTNS0005\",\"details\":[\"Formato de data inválido. Deve corresponder a: yyyy-MM-dd\"]}"
                    )));
        }
    }
}