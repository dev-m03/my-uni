package com.example.my_lms.service;

import com.example.my_lms.model.userinfo;
import com.example.my_lms.repo.UserInfoRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserInfoService implements UserDetailsService {
@Autowired
private UserInfoRepo repo;
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<userinfo> user = repo.findByUsername(username);
    if (user.isPresent()) {
        var userObj = user.get();
        return User.builder()
                .username(userObj.getUsername())
                .password(userObj.getPassword())
                .build();
    }
    else{
        throw new UsernameNotFoundException(username);
    }
}



}
