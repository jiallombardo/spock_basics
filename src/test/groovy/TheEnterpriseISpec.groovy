import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import spock.lang.Specification;
import spock.lang.Unroll;

import static Clearance.*;

@ContextConfiguration(classes = TheEnterpriseConfiguration.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TheEnterpriseISpec extends Specification {

    @Autowired
    private TheEnterprise testShip;

    private ClearanceProvider testProvider = Mock();

    public void setup() {
        testShip.clearanceProvider = testProvider; //use reflection to insert new field value
    }

    @Unroll
    public void '#title #name has proper access to the Enterprise'() {
        setup: 'a parameterized Officer'
            Officer officer = new Officer(name, title)
            (officer.getName().equals("Spock") ? 0 : 1) * testProvider.getClearance(title) >> testClearanceBehavior(title);


        when: 'get clearance for this officer'
            Clearance result = testShip.clearanceToSystems(officer);

        then: 'the clearance level is correct'
            result == expectedResult;

        where:
            name     | title           || expectedResult
            "Kirk"   | "Captain"       || UNRESTRICTED
            "Spock"  | "Intruder"      || UNRESTRICTED
            "Spock"  | "First Officer" || UNRESTRICTED
            "Kirk"   | "Intruder"      || UNAUTHORIZED
            "Scotty" | "First Officer" || LIMITED
            "Nyota"  | "Lieutenant"    || null
    }

    public Clearance testClearanceBehavior(String officerTitle) {
        switch (officerTitle) {
            case "Captain":
                return UNRESTRICTED;

            case "First Officer":
                return LIMITED;

            case "LieutenantCommander":
                return LIMITED;

            case "Intruder":
                return UNAUTHORIZED;
        }

        return null;
    }
}
