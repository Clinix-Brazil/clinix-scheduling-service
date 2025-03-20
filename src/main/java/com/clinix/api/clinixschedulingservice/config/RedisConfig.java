package com.clinix.api.clinixschedulingservice.config;

import com.clinix.api.dto.ClinicaRmiDTO;
import com.clinix.api.dto.MedicoRmiDTO;
import com.clinix.api.dto.PacienteRmiDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, MedicoRmiDTO> medicoRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, MedicoRmiDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(MedicoRmiDTO.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, PacienteRmiDTO> pacienteRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, PacienteRmiDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(PacienteRmiDTO.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, ClinicaRmiDTO> clinicaRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ClinicaRmiDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(ClinicaRmiDTO.class));
        return template;
    }
}
