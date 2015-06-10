import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Configuration
public class FooBarConfiguration {

    /**
     * A context FooBar bean with a Proxy Foo implementation.
     * We avoid writing the implementation of Foo.
     */
    @Bean
    FooBar fooBar() {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        };
        Foo proxyFoo = (Foo)Proxy.newProxyInstance(Foo.class.getClassLoader(),
                new Class[]{Foo.class},
                handler);
        FooBar fooBar = new FooBar(proxyFoo);

        return fooBar;
    }
}
