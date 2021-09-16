package cnj.kubernetes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KubernetesApplication {

    public static void main(String[] args) {
        SpringApplication.run(KubernetesApplication.class, args);
    }

    @Bean
    ApplicationRunner genericRunner(@Value("${cnj.message}") String message) {
        return args -> System.out.println("running everywhere. Here's the message: " + message);
    }

    @Bean
    @ConditionalOnCloudPlatform(CloudPlatform.KUBERNETES)
    ApplicationRunner kubernetesRunner() {
        return args -> System.out.println("running inside a Kubernetes environment");
    }
}
