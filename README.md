# Basic Configuration 

This example introduces a few key concepts:

# one
 * 12 factor configuration 
 * JNDI, env vars, `application.yaml`, `application.properties`, `@Value`, 
 * Property Placeholder resolution, default values, etc. 
 * `@ConfigurationProperties`

# two
 * the `Environment` abstraction 
 * `PropertySource` thats registered in `spring.factories` as an `EnvironmentPostProcessor` 


# three
 * the refresh Actuator endpoint: `curl localhost:8080/actuator/refresh -d {} -H "Content-Type: application/json" `
 * `@RefreshScope`
 * Spring Cloud Bus 
 * Spring Cloud Config Monitor (which detects changes in file systems or responds to webhooks from providers like github or gitlab)
    * https://cloud.spring.io/spring-cloud-config/multi/multi__push_notifications_and_spring_cloud_bus.html
 * Spring Cloud Vault  


https://springbootdev.com/2018/07/17/spring-cloud-config-refreshing-the-config-changes-with-spring-cloud-bus-part-2/
