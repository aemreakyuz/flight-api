package com.flightapp.flightapi.config;


import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
//        return httpSecurity.csrf(csrf-> csrf.disable())
//                .authorizeHttpRequests(auth-> {
//                    auth.requestMatchers("/airport/**").permitAll();
//                    auth.anyRequest().authenticated();
//                })
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults())
//                .build();
//    }

//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }


//    @Bean
//    public AuthenticationManager authManager(UserDetailsService userDetailsService){
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return new ProviderManager(provider);
//    }


}
