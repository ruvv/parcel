package io.ruv.proto.config;

import io.ruv.proto.user.api.courier.CourierStatus;
import io.ruv.proto.user.entity.CourierExtension;
import io.ruv.proto.user.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.SecureRandom;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(11, new SecureRandom());
    }

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {

        var mockUser = new User()
                .setUsername("admin")
                .setRoles(List.of("admin", "courier", "customer"))
                .setCourierExtension(new CourierExtension()
                        .setStatus(CourierStatus.OFF_DUTY));

        http.cors().and()
                .csrf().disable()
                .anonymous(httpSecurityAnonymousConfigurer ->
                        httpSecurityAnonymousConfigurer.principal(mockUser))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/v1/customer").hasAuthority("SCOPE_customer")
//                        .requestMatchers("/api/v1/courier").hasAuthority("SCOPE_courier")
//                        .requestMatchers("/api/v1/parcels").hasAuthority("SCOPE_admin")
//                        .requestMatchers("/api/v1/couriers").hasAuthority("SCOPE_admin")
//                        .requestMatchers(HttpMethod.POST, "/api/v1/customer/register").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
//                        .anyRequest().authenticated());

        return http.build();
    }
}
