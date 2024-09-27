package com.example.springbootlogin.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String s) {
        // TODO: Regex to validate email
        // assume that every email is valid
        return true;
    }
}
