package com.my.stock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    private static final String REDIS_HOST_PREFIX = "redis://";

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress(REDIS_HOST_PREFIX + "localhost:6379");
        return Redisson.create(config);
    }
}
