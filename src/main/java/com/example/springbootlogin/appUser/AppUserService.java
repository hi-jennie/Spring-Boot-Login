package com.example.springbootlogin.appUser;

import com.example.springbootlogin.registration.token.ConfirmationToken;
import com.example.springbootlogin.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    @Override
    // loadUserByUsername 方法：这个方法在用户登录时被调用，它接收一个邮箱（或用户名）作为参数，去数据库中查找用户。
    // 如果找到了用户，就返回用户的详细信息。如果没找到，就抛出异常，表示用户不存在。
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).
                orElseThrow(()->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG,email)));
    }

    public String signUpUser(AppUser appUser){
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if(userExists){
            throw new IllegalStateException("email already taken");
        }
        // encode the password and save it to the database
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
        // create a token
        // the token is used to confirm if the email is valid
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // TODO: send email

        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
    /* the whole logic of the registration process:
    1.	Check if the user exists: Verify if the email is already registered.
	2.	Encode and save the password: Securely encode the password and save the user to the database.
	3.	Generate a confirmation token: Create a unique token and associate it with the user.
	4.	Send the confirmation token via email: (Not implemented yet) Send the token to the user for email confirmation.
	5.	Enable the user account: Upon email confirmation, update the user’s status to activate the account.
     */
}
