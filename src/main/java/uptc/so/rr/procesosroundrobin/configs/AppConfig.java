package uptc.so.rr.procesosroundrobin.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @implNote This class is used to configure the spring context for dependency injection
 */
@Configuration(proxyBeanMethods=false)
@ComponentScan(basePackages = "uptc.so.rr.procesosroundrobin")
public class AppConfig {
}
