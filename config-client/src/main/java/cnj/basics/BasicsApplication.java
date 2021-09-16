package cnj.basics;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

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


@Controller
@ResponseBody
class PropertyController {

    private final Environment environment;
    private final CnjProperties properties;

    PropertyController(CnjProperties properties,
                       Environment environment) {
        this.environment = environment;
        this.properties = properties;
        System.out.println("the environment reports " + read());
        System.out.println("the property binding reports " + properties.getMessage());
    }

    @GetMapping("/read")
    String read() {
        return this.environment.getProperty("cnj.message");
    }
}