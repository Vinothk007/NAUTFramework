package cucumberExecutor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import com.framework.utils.GlobalVariables;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCase;

public class StepDefBeginEndLogger {
	
	

private int currentStepDefIndex = 0;
//public static String currentStepDescr;
@BeforeStep
public void doSomethingBeforeStep(Scenario scenario) throws Exception {

	    Field f = scenario.getClass().getDeclaredField("delegate");
	    f.setAccessible(true);
	    TestCaseState tcs = (TestCaseState) f.get(scenario);

	    Field f2 = tcs.getClass().getDeclaredField("testCase");
	    f2.setAccessible(true);
	    TestCase r = (TestCase) f2.get(tcs);

	        List<PickleStepTestStep> stepDefs = r.getTestSteps()
	                .stream()
	                .filter(x -> x instanceof PickleStepTestStep)
	                .map(x -> (PickleStepTestStep) x)
	                .collect(Collectors.toList());


	        PickleStepTestStep currentStepDef = stepDefs
	                .get(currentStepDefIndex);
	       String currentStepDescr = currentStepDef.getStep().getText();
	        GlobalVariables.currentStep.set(currentStepDescr);
	        currentStepDefIndex += 1;
	     
}

@AfterStep
public void doSomethingAfterStep(Scenario scenario) {
    //currentStepDefIndex += 1;
}
}