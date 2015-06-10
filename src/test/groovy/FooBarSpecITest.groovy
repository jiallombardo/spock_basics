import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by user on 09.06.2015.
 */
@ContextConfiguration(classes = FooBarConfiguration.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
class FooBarSpecITest extends Specification {

    @Autowired
    private FooBar testSubject;

    private Foo testFoo = Mock();

    public void setup() {
        testSubject.foo = testFoo; //use reflection to insert new field value
    }

    @Unroll
    public void 'important logic works correctly with input string #string, integer value #value and seed #seed'() {
        setup: 'a parameterized Bar object'
            Bar bar = new Bar(string, value);
            testFoo.getNumericSeed(_) >> seed;

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
}
