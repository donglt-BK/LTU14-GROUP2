package com.bk.olympia;

import com.bk.olympia.model.entity.User;
import com.bk.olympia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@SpringBootApplication
@EnableWebMvc
public class OlympiaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(OlympiaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
		clearLobby();
    }

    @Autowired
    private UserRepository userRepository;

    public void clearLobby() {
        List<User> user = userRepository.findAll();
		for (User u : user) {
			u.setLobbyId(-1);
		}
		userRepository.saveAll(user);
    }
}
