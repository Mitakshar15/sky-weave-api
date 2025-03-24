package org.skyweave.service.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;

@Configuration
@ComponentScan({"org.skyweave"})
@EnableTransactionManagement
@EnableConfigurationProperties
public class AppConfig {

  @Bean
  GroupedOpenApi apiV1() {
    return GroupedOpenApi.builder().group("v1").pathsToMatch("/v1/**").build();
  }

  @Bean
  OpenAPI rideShareUserApiV1() {
    return new OpenAPI().info(new Info().title("Sky Weave API")
        .description("Api docs for Sky Weave application").version("0.0.1"));
  }

}
