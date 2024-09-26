package com.example.springbootlogin;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// this class is specific for spring security
// how to find users once we try to log in
// @Service告诉 Spring 这个类是一个服务类，它负责处理与业务逻辑相关的工作
@Service
@AllArgsConstructor
// UserDetailsService 接口：这是 Spring Security 的一部分，用来根据用户名或邮箱来查找用户信息。
// AppUserService 实现了这个接口，所以它负责从数据库中找用户。
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AppUserRepository appUserRepository;
    @Override
    // loadUserByUsername 方法：这个方法在用户登录时被调用，它接收一个邮箱（或用户名）作为参数，去数据库中查找用户。
    // 如果找到了用户，就返回用户的详细信息。如果没找到，就抛出异常，表示用户不存在。
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).
                orElseThrow(()->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG,email)));
    }
}
