package br.com.golfinvest.data.config;

import br.com.golfinvest.data.view.MainFrame;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:default.properties")
public class SpringContext {

    @Value("${title}")
    String title;

    @Value("${aws}")
    String aws;

    @Value("${user}")
    String user;

    @Value("${pass}")
    String pass;

    @Bean(name = "mainFrame")
    public MainFrame createMainFrame() {
        return new MainFrame(title, aws, user, pass);
    }

    @Bean(name = "aws")
    public String CreateAWS(){
        return aws;
    }

    @Bean(name = "user")
    public String CreateUser(){
        return user;
    }

    @Bean(name = "pass")
    public String CreatePass(){
        return pass;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer setUp() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
