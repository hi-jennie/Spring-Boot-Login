package com.example.springbootlogin.security.config;

import com.example.springbootlogin.appUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig{
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v*/registration/**").permitAll()  // 允许所有注册请求
                        .anyRequest().authenticated()  // 其他请求需要认证
                )
                .csrf(csrf -> csrf.disable())  // 禁用 CSRF
                .formLogin(form -> form   // 使用新的配置方式来启用表单登录
                        .loginPage("/login")  // 可以自定义登录页面路径
                        .permitAll()  // 允许所有人访问登录页面
                );
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
//   has already been discarded ever since Spring Security 5.7
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//         http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/api/v*/registration/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin();
//    }
}
