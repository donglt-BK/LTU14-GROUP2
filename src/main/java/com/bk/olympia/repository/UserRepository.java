package com.bk.olympia.repository;

import com.bk.olympia.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);
    User findById(int id);
    User findByName(String name);

    @Modifying
    @Query("UPDATE User u SET u.gender = ?1 WHERE u.id = ?2")
    void setUserGenderById(int gender, int id);

    @Modifying
    @Query("UPDATE User u SET u.name = ?1 WHERE u.id = ?2")
    void setUserNameById(String name, int id);
}
