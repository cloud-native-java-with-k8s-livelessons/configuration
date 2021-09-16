# Basic Configuration 

This example introduces a few key concepts:

 * the `Environment` abstraction 
 * `PropertySource`
 * `@ConfigurationProperties`
 * the refresh Actuator endpoint: `curl localhost:8080/actuator/refresh -d {} -H "Content-Type: application/json" `
 * `@RefreshScope`
 * Spring Cloud Bus 
 * Spring Cloud Config Monitor (which detects changes in file systems or responds to webhooks from providers like github or gitlab)
    * https://cloud.spring.io/spring-cloud-config/multi/multi__push_notifications_and_spring_cloud_bus.html
 * Spring Cloud Vault  


https://springbootdev.com/2018/07/17/spring-cloud-config-refreshing-the-config-changes-with-spring-cloud-bus-part-2/

//1. change the code to read every time we do getProperty()
// 2. then add a controller that caches the value on injection or in the binding 
//3 . then show how thats ineffcieint 
// 4. then create an endpoint that triggers the contextRefresher and add code to listen to the EnvironmentChangeEvent
// 5. then show the Sproing Boot Actuator 
// 6. then ask what about @CondfigurationProperties and other objects? 
// 7 then show @RefreshScope and RefreshScopeRefreshEvent.class 

@Controller
@ResponseBody
class MessageController {


    private String message;

    private final Environment environment;

    @EventListener(EnvironmentChangeEvent.class)
    public void refresh() {
        this.message = read();
    }

    public String read() {
        return environment.getProperty("cnj.message");
    }

    private final ContextRefresher refresher;

    @GetMapping("/up")
    void update() {
        this.refresher.refresh();
    }

    MessageController(Environment environment, ContextRefresher refresher) {
        this.environment = environment;
        this.refresher = refresher;
        refresh();
    }

    @GetMapping("/message")
    String message() {
        return this.message;
    }
}