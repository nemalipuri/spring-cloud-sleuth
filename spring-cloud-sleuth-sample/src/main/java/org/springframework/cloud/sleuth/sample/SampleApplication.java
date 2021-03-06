package org.springframework.cloud.sleuth.sample;

import java.util.Random;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Spencer Gibb
 */
@Configuration
@EnableAutoConfiguration
@RestController
@Slf4j
public class SampleApplication implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

	public static final String CLIENT_NAME = "testApp";

    @Autowired
    private RestTemplate restTemplate;
    private int port;

    @SneakyThrows
    @RequestMapping("/")
	public String hi() {
        final Random random = new Random();
        Thread.sleep(random.nextInt(1000));

        String s = restTemplate.getForObject("http://localhost:" + port + "/hi2", String.class);
        return "hi/"+s;
	}

    @SneakyThrows
    @RequestMapping("/hi2")
    public String hi2() {
        final Random random = new Random();
        Thread.sleep(random.nextInt(1000));
        return "hi2";
    }

	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

    /*@Bean
    public SpanCollector spanCollector() {
        return new LoggingSpanCollectorImpl();
    }*/

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        port = event.getEmbeddedServletContainer().getPort();
    }
}
