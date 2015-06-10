import java.util.LinkedList;
import java.util.List;

/**
 * An instance with very important business logic.
 */
public class FooBar {

    private Foo foo;

    public FooBar(Foo foo) {
        this.foo = foo;
    }

    public int doImportantLogic(Bar bar) {
        String barString = bar.getStringField();
        int barInt = bar.getIntField();
        int divider = barInt == 0 ? 1 : foo.getNumericSeed(barString.length());
        return barInt / divider + barString.length();
    }

    public List<Bar> createBars(int barCount) {
        List<Bar> outputList = new LinkedList<>();
        for (int i = 0; i < barCount; i++) {
            outputList.add(new Bar(String.valueOf(i), i));
        }

        return outputList;
    }
}
