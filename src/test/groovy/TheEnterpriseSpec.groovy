import spock.lang.Ignore
import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll;

import static Clearance.LIMITED
import static Clearance.UNAUTHORIZED;
import static Clearance.UNRESTRICTED;

public class TheEnterpriseSpec extends Specification {

    private static final String TEST_TRAINEE_TITLE = "Test Trainee";
    private static final String THE_ENTERPRISE_IS_DOCKED = "enterprise.docked";
    static {
        System.setProperty(THE_ENTERPRISE_IS_DOCKED, "false");
    }

    @Shared
    private Officer officerKirk;
    @Shared
    private Officer officerSpock;
    @Shared
    private Officer officerMccoy;

    private TheEnterprise ourShip;
    private ClearanceProvider testProvider = Mock();

    public void setupSpec() {
        officerKirk = new Officer("Kirk", "Captain");
        officerSpock = new Officer("Spock", "First Officer");
        officerMccoy = new Officer("McCoy", "Lieutenant Commander");
    }

    public void setup() {
        ourShip = new TheEnterprise(TEST_TRAINEE_TITLE, testProvider);
    }

    @Unroll
    public void '#title #name has proper access to the Enterprise'() {
        setup: 'a parameterized Officer'
            Officer officer = new Officer(name, title)
            (officer.getName().equals("Spock") ? 0 : 1) * testProvider.getClearance(title) >> testClearanceBehavior(title);


        when: 'get clearance for this officer'
            Clearance result = ourShip.clearanceToSystems(officer);

        then: 'the clearance level is correct'
            result == expectedResult;

        where:
            name                   | title                   || expectedResult
            officerKirk.getName()  | officerKirk.getTitle()  || UNRESTRICTED
            officerSpock.getName() | "Intruder"              || UNRESTRICTED
            officerSpock.getName() | officerSpock.getTitle() || UNRESTRICTED
            officerKirk.getName()  | "Intruder"              || UNAUTHORIZED
            "Scotty"               | "First Officer"         || LIMITED
            "Nyota"                | "Lieutenant"            || null
    }

    @Ignore
    public void 'access clearance to ship\'s systems is determined correctly'() {
        given: 'input is a random officer'
            Officer officer = officerKirk;

        when: 'clearance is requested for this officer'
            Clearance clearance = ourShip.clearanceToSystems(officer);

        then: 'the officer is given proper access according to his parameters'
            clearance == LIMITED;
            1 * testProvider.getClearance(_) >> LIMITED;
    }

    @Ignore
    public void 'Spock requires unrestricted access now!'() {
        given: 'officer Spock'
            Officer spock = officerSpock;

        when: 'Spock requires clearance to ship\'s systems'
            Clearance clearance = ourShip.clearanceToSystems(spock);

        then: 'Spock is cleared without any additional services being invoked'
            clearance == UNRESTRICTED;
            0 * testProvider.getClearance("Spock");

    }

    public void 'Officer McCoy has a special greeting'() {
        given: 'officer McCoy'
            Officer mccoy = officerMccoy;

        when: 'McCoy requests clearance'
            ourShip.clearanceToSystems(mccoy);

        then: 'He gets an exception with a special greeting'
            AssertionError greeting = thrown();
            greeting.getMessage().equals("Really, Dr. McCoy. You must learn to govern your passions; they will be your undoing. Logic suggests...");
    }

    @IgnoreIf({ System.getProperty(THE_ENTERPRISE_IS_DOCKED).equals("false") })
    public void 'officers are trained at the enterprise'() {
        given: 'we have a number of people to train'
            int numberOfPeopleToTrain = 3;

        when: 'we train these people'
            List<Officer> officers = ourShip.trainOfficers(numberOfPeopleToTrain);

        then: 'we receive proper officers with proper titles and names'
            officers.size() == numberOfPeopleToTrain;
            for (int i = 0; i < numberOfPeopleToTrain; i++) {
                verifyNewOfficer(i, officers);
            }

        when: 'we have no one to train'
            List<Officer> noOfficers = ourShip.trainOfficers(0);

        then: 'we receive no officers'
            noOfficers.isEmpty();
    }

    private void verifyNewOfficer(int i, List<Officer> officers) {
        Officer toCheck = officers.get(i);
        assert toCheck.getName().equals(TEST_TRAINEE_TITLE + " " + i);
        assert toCheck.getTitle().equals(TEST_TRAINEE_TITLE);
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
