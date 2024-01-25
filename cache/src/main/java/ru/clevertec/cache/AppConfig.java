package ru.clevertec.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.cache.impl.CacheFactoryImpl;

@Configuration
public class AppConfig {

    @Bean
    @ConfigurationProperties("cache")
    public <K,V> CacheFactory<K,V> getCacheFactory(){
        return new CacheFactoryImpl<>();
    }
}
