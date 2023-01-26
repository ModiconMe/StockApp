package io.modicon.priceservice.infrastructure.config;

import io.modicon.cqrsbus.Bus;
import io.modicon.cqrsbus.DefaultBus;
import io.modicon.cqrsbus.Registry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@RequiredArgsConstructor
@Configuration
@EnableFeignClients(basePackages = "io.modicon.priceservice.application.client")
@EnableScheduling
public class ApplicationConfig {

    private final ApplicationContext applicationContext;

    @Bean
    public Bus bus() {
        return new DefaultBus(new Registry(applicationContext));
    }

    @Value("${spring.redis.host}")
    private String url;

    @Value("${spring.redis.port}")
    private Integer port;

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        return new RedisStandaloneConfiguration(url, port);
    }

//    @Bean
//    public ClientOptions clientOptions() {
//        return ClientOptions.builder()
//                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
//                .autoReconnect(true)
//                .build();
//    }
//
//    @Bean
//    public RedisConnectionFactory connectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration) {
//
//        LettuceClientConfiguration configuration = LettuceClientConfiguration.builder()
//                .clientOptions(clientOptions()).build();
//
//        return new LettuceConnectionFactory(redisStandaloneConfiguration, configuration);
//    }

//    @Bean
//    @ConditionalOnMissingBean(name = "redisTemplate")
//    @Primary
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        StringRedisTemplate template = new StringRedisTemplate();
//        template.setConnectionFactory(redisConnectionFactory);
//        return template;
//    }

}