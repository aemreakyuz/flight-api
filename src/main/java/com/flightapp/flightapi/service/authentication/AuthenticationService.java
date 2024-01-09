package com.flightapp.flightapi.service.authentication;

import com.flightapp.flightapi.dto.RegisterUser;
import com.flightapp.flightapi.entity.user.ApplicationUser;
import com.flightapp.flightapi.entity.user.Role;
import com.flightapp.flightapi.repository.RoleRepository;
import com.flightapp.flightapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUser register(RegisterUser registerUser){

        String encodedPassword = passwordEncoder.encode(registerUser.password());

        Role userRole = roleRepository.findByAuthority("USER").get();
        Role adminRole = roleRepository.findByAuthority("ADMIN").get();

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        roles.add(adminRole);

        ApplicationUser user = new ApplicationUser();
        user.setFullName(registerUser.fullName());
        user.setEmail(registerUser.email());
        user.setPassword(encodedPassword);

        switch(registerUser.role()){
            case "ADMIN":
                Optional <Role>  role = roleRepository.findByAuthority("ADMIN");
                if(role.isPresent()){
                    roles.remove(userRole);
                    user.setAuthorities(roles);
                    break;
                }
            case "USER":
                Optional <Role>  usersRole = roleRepository.findByAuthority("USER");
                if(usersRole.isPresent()){
                    roles.remove(adminRole);
                    user.setAuthorities(roles);
                }
                break;
        }

        return userRepository.save(user);
    }

    public List<ApplicationUser> getUserList(){
        return userRepository.findAll();
    }
}
