package ngo.spine.eigenschuldapi.Config;

import ngo.spine.eigenschuldapi.Services.UserDetailsServiceImp;
import ngo.spine.eigenschuldapi.Filter.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.*;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserDetailsServiceImp userDetailsServiceImp;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(UserDetailsServiceImp userDetailsServiceImp, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http = http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
            req->req.requestMatchers("/api/v1/auth/*", "api/v1/auth/register/*").permitAll()
                        .requestMatchers("/api/v1/chart/*").hasAnyAuthority("CLIENT", "HULPVERLENER")
                        .requestMatchers("/api/v1/chart/save/*").hasAnyAuthority("CLIENT", "HULPVERLENER")
                        .requestMatchers("/api/v1/domain/*").hasAnyAuthority("ADMIN")
                        .requestMatchers("/api/v1/domain").hasAnyAuthority("ADMIN")
                        .requestMatchers("/api/v1/letter/*").hasAnyAuthority("CLIENT", "HULPVERLENER")
                        .requestMatchers("/api/v1/questionary/*").hasAnyAuthority("CLIENT", "HULPVERLENER")
                        .requestMatchers("/api/v1/question/response/*").hasAnyAuthority("CLIENT", "HULPVERLENER")
                        .requestMatchers("/api/v1/question/response/delete/*").hasAnyAuthority("CLIENT", "HULPVERLENER")
                        .requestMatchers("/api/v1/question/response/update/*").hasAnyAuthority("CLIENT", "HULPVERLENER")
                        .requestMatchers("/api/v1/user/addExercise/*").hasAnyAuthority("HULPVERLENER", "CLIENT")
                        .requestMatchers("/api/v1/user/hulpverleners").hasAnyAuthority("ORGANISATIE", "ADMIN")
                        .requestMatchers("/api/v1/user/hulpverleners/*").hasAnyAuthority("ORGANISATIE", "ADMIN")
                        .requestMatchers("/api/v1/invite/send/*").hasAnyAuthority("HULPVERLENER")
                        .requestMatchers("/api/v1/clientprogress/*").hasAnyAuthority("HULPVERLENER")
                        .anyRequest()
                        .authenticated()
            ).userDetailsService(userDetailsServiceImp)
            .sessionManagement(session->session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
