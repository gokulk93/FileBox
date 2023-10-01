package com.tectot.filebox.security;

import com.tectot.filebox.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email does not exist"));
        return new CustomUserDetails(user.getEmail(), user.getPassword(), user.getUserRole(), user.getOrganisation().getName());
    }
}
