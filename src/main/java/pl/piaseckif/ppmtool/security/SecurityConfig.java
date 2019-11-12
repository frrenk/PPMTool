package pl.piaseckif.ppmtool.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.piaseckif.ppmtool.services.CustomUserDetailsService;

import static pl.piaseckif.ppmtool.services.SecurityConstants.H2_URL;
import static pl.piaseckif.ppmtool.services.SecurityConstants.SIGN_UP_URL;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public JwtAuthenticationFilter authenticationFilter() {
        return  new JwtAuthenticationFilter();
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .headers().frameOptions().sameOrigin().and() //enabling H2
                .authorizeRequests()
                    .antMatchers("/",
                            "favicon.ico",
                            "**/*.png",
                            "**/*.gif",
                            "**/*.svg",
                            "**/*.jpg",
                            "**/*.html",
                            "**/*.css",
                            "**/*.js"
                    ).permitAll()
                .antMatchers(SIGN_UP_URL).permitAll()
                .antMatchers(H2_URL).permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
