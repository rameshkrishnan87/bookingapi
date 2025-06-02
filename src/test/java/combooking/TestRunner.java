package combooking;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        publish = true,
        features = "src/test/java/combooking/features",
        glue = "combooking.step_definitions", dryRun=true,
        plugin = {"pretty", "html:target/cucumber-reports.html"},
        monochrome = true,
        tags = "@business or @happyPathScenario or @error"

)

public class TestRunner {

}