package pl.com.tt.intern.soccer.configuration;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
import java.util.Timer;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class AppConfiguration {


    @Value("${server.default.timezone}")
    private String defaultTimeZone;

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(defaultTimeZone));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Timer timer(){ return new Timer();}
}
