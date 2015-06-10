import spock.lang.Ignore
import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

public class FooBarSpec extends Specification {

    private static final String RUN_TEST_PROPERTY = "run.bar.creation.test";
    static {
        System.setProperty(RUN_TEST_PROPERTY, "true");
    }

    @Shared
    private Bar barRequiresSeed;
    @Shared
    private Bar barNotRequiresSeed;

    private FooBar testSubject;
    private Foo testFoo = Mock();

    public void setupSpec() {
        barRequiresSeed = new Bar("Baz", 1);
        barNotRequiresSeed = new Bar("Baz", 0);
    }

    public void setup() {
        testSubject = new FooBar(testFoo);
    }

    @Ignore
    public void 'important logic works correctly'() {
        given: 'a basic Bar object that requires numeric seed'
            Bar bar = barRequiresSeed;

        when: 'we run the given object through our business logic'
            int result = testSubject.doImportantLogic(bar);

        then: 'we obtain a necessary result and Foo is invoked'
            result == 4;
            1 * testFoo.getNumericSeed(_) >> 1
    }

    @Unroll
    public void 'important logic works correctly with input string #string, integer value #value and seed #seed'() {
        setup: 'a parameterized Bar object'
            Bar bar = new Bar(string, value);
            (value == 0 ? 0 : 1) * testFoo.getNumericSeed(_) >> seed;

        when: 'we run the given object through our business logic'
            int result = testSubject.doImportantLogic(bar);

        then: 'we obtain a correct result based on given parameter'
            result == expectedResult;

        where:
            string   | value | seed || expectedResult
            "Baz"    | 0     | 1000 || 3
            "Bazz"   | 1     | 1000 || 4
            "Baz"    | 0     | 1    || 3
            "Foo"    | 0     | 1    || 3
            "Bazz"   | 0     | 1    || 4
            "Fuzz"   | 0     | 1    || 4
            "Bazz"   | 1     | 1    || 5
            "Bazz"   | 2     | 1    || 6
            "Foobar" | 0     | 1    || 6
            "Foobar" | 3     | 1    || 9
            "Foo"    | 3     | 1    || 6
            "Baz"    | 3     | 1    || 6
            "Bazz"   | 3     | 1    || 7
    }

    @Ignore
    public void 'important logic does not use numeric seed when the Bar\'s integer field is 0'() {
        given: 'a Bar object that doesn\'t require numeric seed'
            Bar bar = barNotRequiresSeed;

        when: 'we run the given object through important logic'
            int result = testSubject.doImportantLogic(bar);

        then: 'we obtain a necessary result and Foo is not invoked'
            result == 3;
            0 * testFoo.getNumericSeed(_);
    }

    public void 'important logic crashes when the numeric seed is zero'() {
        setup: 'we have an arbitrary Bar object and we say that testFoo returns zero'
            Bar bar = barRequiresSeed;
            testFoo.getNumericSeed(_) >> 0;

        when: 'we run important logic with the given setup'
            testSubject.doImportantLogic(bar);

        then: 'we get a division by zero exception'
            ArithmeticException thrown = thrown();
            thrown.getMessage().equals("/ by zero");
    }

    @IgnoreIf({ System.getProperty(RUN_TEST_PROPERTY).equals("true") })
    public void 'a correct list of Bars is returned for the given number'() {
        given: 'a sample number of Bar objects we will be creating'
            int testSample = 3;

        when: 'the createBars method is invoked, we get a list of Bar objects of testSample size'
            List<Bar> result = testSubject.createBars(testSample);

        then:
            result.size() == testSample;
            for (int i = 0; i < testSample; i++) {
                Bar testBar = result.get(i);
                assert testBar.getIntField() == i;
                assert testBar.getStringField() == String.valueOf(i);
            }
    }
}
