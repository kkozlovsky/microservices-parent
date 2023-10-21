package ru.kerporation.discoveryserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Value("${app.eureka.username}")
    private String username;
    @Value("${app.eureka.password}")
    private String password;

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        final UserDetails user = User.withDefaultPasswordEncoder()
                .username(username)
                .password(password)
                .roles("USER")
                .authorities("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

//    @Override
//    protected void configure(final HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests().anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();
//    }


    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz.anyRequest().authenticated())
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
//                .ignoringRequestMatchers("/eureka/**");
        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/eureka/**");
    }
}
