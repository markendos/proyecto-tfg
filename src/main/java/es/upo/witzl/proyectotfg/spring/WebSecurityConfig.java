package es.upo.witzl.proyectotfg.spring;

import es.upo.witzl.proyectotfg.users.security.CustomAuthenticationFailureHandler;
import es.upo.witzl.proyectotfg.users.security.CustomLoginAuthenticationSuccessHandler;
import es.upo.witzl.proyectotfg.users.security.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = {"es.upo.witzl.proyectotfg.users.security"})
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomLoginAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    public WebSecurityConfig() {
        super();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .requiresChannel()
                .anyRequest()
                .requiresSecure()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/console*", "/console/**", "/admin/**").hasRole("ADMIN")
                .antMatchers("/user/savePassword*").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
                .antMatchers("/", "/login*", "/logout*", "/signUp*", "/user/registration*","/badUser*",
                        "/emailError*", "/successRegister*", "/registrationConfirm*", "/resetPassword*", "/user/resetPassword*",
                        "/user/changePassword*", "/user/resendRegistrationToken*", "/oauth_login*").permitAll()
                .antMatchers("/invalidSession*", "/test/**").anonymous()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .failureUrl("/login?error=true")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .permitAll()
                .and()
                .sessionManagement()
                .invalidSessionUrl("/invalidSession")
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
                .and()
                .sessionFixation().none()
                .and()
                .logout()
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID","G_AUTHUSER_H")
                .permitAll();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // beans
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
