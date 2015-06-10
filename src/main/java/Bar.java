/**
 * An immutable bean object used by Bar class.
 */
public class Bar {

    private String stringField;
    private int intField;

    public Bar(String strValue, int intValue)
    {
        this.stringField = strValue;
        this.intField = intValue;
    }

    public String getStringField() {
        return stringField;
    }

    public int getIntField() {
        return intField;
    }
}
