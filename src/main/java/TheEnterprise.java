import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A great starship.
 */
public class TheEnterprise {

    private static final String OUR_HERO_SPOCK = "Spock";
    private static final String OFFICER_MCCOY = "McCoy";

    private String newOfficerTitle;
    private ClearanceProvider clearanceProvider;

    public TheEnterprise(String newOfficerTitle, ClearanceProvider clearanceProvider) {
        this.newOfficerTitle = newOfficerTitle;
        this.clearanceProvider = clearanceProvider;
    }

    public Clearance clearanceToSystems(Officer officer) {
        if (OUR_HERO_SPOCK.equals(officer.getName())) {
            return Clearance.UNRESTRICTED;
        }
        assert !OFFICER_MCCOY.equals(officer.getName()) : "Really, Dr. McCoy. You must learn to govern your passions; they will be your undoing. Logic suggests...";
        return clearanceProvider.getClearance(officer.getTitle());
    }

    public List<Officer> trainOfficers(int numberOfOfficers) {
        if (numberOfOfficers < 0)
            return Collections.emptyList();
        List<Officer> trainees = new LinkedList<Officer>();

        for (int i = 0; i < numberOfOfficers; i++) {
            Officer o = new Officer(newOfficerTitle + " " + i, newOfficerTitle);
            trainees.add(o);
        }

        return trainees;
    }
}
