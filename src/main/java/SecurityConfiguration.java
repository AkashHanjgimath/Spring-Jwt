import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;

public class SecurityConfiguration {
    public DefaultSecurityFilterChain securityConfiguration(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry->{
                            registry.requestMatchers("/home", "/register/**", "/authenticate").permitAll();
                            registry.requestMatchers("/admin/**").hasRole("ADMIN");
                            registry.requestMatchers("/user/**").hasRole("USER");
                            registry.anyRequest().authenticated();
                        })
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .build();
    }

}
