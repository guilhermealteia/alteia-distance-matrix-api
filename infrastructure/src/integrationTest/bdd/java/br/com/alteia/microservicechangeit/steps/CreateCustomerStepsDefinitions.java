package br.com.alteia.microservicechangeit.steps;

import br.com.alteia.microservicechangeit.CleanArchitectureChangeItApplication;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.CreateCustomerRequestDto;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static br.com.alteia.common.tests.TestUtils.fromObjectToJson;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {CleanArchitectureChangeItApplication.class})
public class CreateCustomerStepsDefinitions {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private CreateCustomerRequestDto createCustomerRequestDto;

    private ResultActions resultActions;

    private Exception exception = null;

    @Dado("um novo cliente em cadastramento com nome {string}, aniversario {string} e cpf {string}")
    public void umNovoClienteEmCadastramentoComNomeAniversarioECpf(String arg0, String arg1, String arg2) {
        this.createCustomerRequestDto = new CreateCustomerRequestDto(arg0, arg1, arg2);
    }

    @Quando("o cliente confirmar seus dados")
    public void oClienteConfirmarSeusDados() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        }, "/*").build();

        resultActions = mockMvc.perform(post("/v1/api/customers/customer").contentType(MediaType.APPLICATION_JSON)
                .content(fromObjectToJson(createCustomerRequestDto)));
    }

    @Entao("o cadastro do cliente deve ter sido criado")
    public void oCadastroDoClienteDeveTerSidoCriado() throws Exception {
        resultActions.andDo(print()).andExpect(status().isCreated())
                .andExpect(content().string(containsString("{\"id\":")));
    }

    @Entao("o cliente deve receber um erro com o codigo {string} e a mensagem {string}")
    public void oClienteDeveReceberUmErroComAMensagem(String errorCode, String errorMessage) throws Exception {
        String responseBody = resultActions.andDo(print()).andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        assertTrue(responseBody.contains(errorCode));//NOSONAR
        assertTrue(responseBody.contains(errorMessage));//NOSONAR
    }

}
