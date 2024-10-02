package com.example.springbootlogin.appUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
@Repository
//@Transactional(readOnly = true)
public interface AppUserRepository  extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    // @Transactional 保证了在执行 enableAppUser 操作时，整个过程被当作一个完整的事务来看待。
    // 一个事务是指一系列操作，它们要么全部成功，要么全部失败
    @Transactional
    // @Modifying 告诉 Spring 这个方法不仅仅是查询数据，还会修改数据库中的数据。
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);
    // 	1.	使用传入的 email 作为参数。
    //	2.	根据 email 去数据库查找匹配的用户。
    //	3.	将匹配用户的 enabled 字段更新为 TRUE，表示账号已激活。
    //	4.	方法返回一个 int，表示成功更新了多少行数据。
    //  5.  它的实际方法体是一个 JPQL 查询语句，它会更新 AppUser 表中的 enabled 字段。即query中的 SET a.enabled = TRUE WHERE a.email = ?1
}
