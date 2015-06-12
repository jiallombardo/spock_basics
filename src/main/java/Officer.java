/**
 * A possible member of a starship's crew. Officers are immutable - their title
 * doesn't change, as we are not considering long periods of service right now.
 */
public class Officer {
    private String name;
    private String title;

    public Officer(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }
}
