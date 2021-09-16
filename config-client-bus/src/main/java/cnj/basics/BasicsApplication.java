package cnj.basics;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableConfigurationProperties(CnjProperties.class)
public class BasicsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicsApplication.class, args);
    }

}

@Data
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("cnj")
@RefreshScope
class CnjProperties {
    private final String message;
}

@Component
@RequiredArgsConstructor
class PropertyListener {

    private final CnjProperties properties;

    private final Environment environment ;

    @EventListener({ApplicationReadyEvent.class,
            RefreshScopeRefreshedEvent.class})
    public void refresh() {
        System.out.println("the new value is " + this.properties.getMessage());
        System.out.println("the new value is " + this.environment.getProperty("cnj.message"));
    }
}