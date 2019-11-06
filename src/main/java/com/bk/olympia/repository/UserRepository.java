package com.bk.olympia.repository;

import com.bk.olympia.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsername(String username);
    User findById(int id);

    Optional<User> findByName(@NotNull String name);

    @Modifying
    @Query("UPDATE User u SET u.gender = ?1 WHERE u.id = ?2")
    void setUserGenderById(int gender, int id);

    @Modifying
    @Query("UPDATE User u SET u.name = ?1 WHERE u.id = ?2")
    void setUserNameById(String name, int id);
}
