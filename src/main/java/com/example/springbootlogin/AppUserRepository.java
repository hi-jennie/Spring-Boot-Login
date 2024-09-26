package com.example.springbootlogin;

import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
//@Transactional(readOnly = true)
public interface AppUserRepository {
    Optional<AppUser> findByEmail(String email);
}
