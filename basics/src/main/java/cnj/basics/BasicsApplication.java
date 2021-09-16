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

@Component
@RequiredArgsConstructor
class ConfigurationHasChangedListener {

    private final Environment environment;

    @EventListener
    public void refresh(RefreshScopeRefreshedEvent event) {
        System.out.println("------------------");
        System.out.println(event.toString());
        System.out.println(this.environment.getProperty("cnj.message"));
    }
}


class CnjEnvironmentInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final File file = new File(System.getenv().get("HOME") + "/Desktop/config.properties");

    private final CnjPropertySource cps = new CnjPropertySource(file);

    CnjEnvironmentInitializer() {
        System.out.println("new CnjEnvironmentInitializer");
    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        context.addApplicationListener(this.cps);
        context.getEnvironment().getPropertySources().addFirst(this.cps);
    }

}

class CnjPropertySource extends PropertySource<String>
        implements ApplicationListener<RefreshScopeRefreshedEvent> {

    private final File config;

    private final AtomicReference<Properties> properties = new AtomicReference<>();

    public CnjPropertySource(File config) {
        super("cnj");
        this.config = config;
        this.initialize();
    }

    @Override
    public Object getProperty(String s) {
        return properties.get().getProperty(s);
    }


    @SneakyThrows
    private void initialize() {
        System.out.println("reinitializing the property source...");
        try (var in = new BufferedInputStream(new FileInputStream(this.config))) {
            var properties = new Properties();
            properties.load(in);
            this.properties.set(properties);
        }
    }

    @Override
    public void onApplicationEvent(RefreshScopeRefreshedEvent refreshScopeRefreshedEvent) {
        this.initialize();
    }
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