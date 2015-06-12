import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Configuration
public class TheEnterpriseConfiguration {

    /**
     * A context {@link TheEnterprise} bean with a Proxy {@link ClearanceProvider} implementation.
     * We avoid writing the implementation of {@link ClearanceProvider}.
     */
    @Bean
    TheEnterprise theShip() {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        };
        ClearanceProvider proxyProvider = (ClearanceProvider) Proxy.newProxyInstance(ClearanceProvider.class.getClassLoader(),
                new Class[]{ClearanceProvider.class},
                handler);
        TheEnterprise ship = new TheEnterprise("trainee", proxyProvider);

        return ship;
    }
}
