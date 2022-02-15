package br.com.alteia.microservicechangeit;

import io.cucumber.core.cli.Main;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-report.html"},
        features = {"src/integrationTest/resources/cucumber"}
)
class RunCucumberTest {

    public static final int SUCCESS_EXIT_STATUS = 0;

    @ParameterizedTest
    @ValueSource(strings = {"CreateCustomerIntegration.feature"})
    public void runCucumberTest(String testFile) {
        String[] argv = new String[]{"-g", "", "./src/integrationTest/resources/cucumber/" + testFile};
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        byte exitstatus = Main.run(argv, contextClassLoader);
        assertEquals(SUCCESS_EXIT_STATUS, exitstatus);//NOSONAR
    }
}