package com.esgi.studyBuddy.init;

import com.esgi.studyBuddy.model.*;
import com.esgi.studyBuddy.repository.UserRepository;
import com.esgi.studyBuddy.service.RoomMessageService;
import com.esgi.studyBuddy.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomTestRunner implements CommandLineRunner {

    private final RoomService roomService;
    private final UserRepository userRepository;
    private final RoomMessageService roomMessageService;

    @Override
    public void run(String... args) {
        User user = new User();
        user.setEmail("owner@example.com");
        user.setPassword("dummy");
        user.setDisplayName("Owner");
        user.setVerified(true);
        user = userRepository.save(user);

        Room room = Room.builder()
                .owner(user)
                .subject("Math")
                .level("Beginner")
                .topic("Algebra")
                .build();

        roomService.createRoom(room);
        System.out.println("Room created successfully.");
        // 3. Post regular message
        roomMessageService.saveMessage(room.getId(), user.getId(), "This is a regular message.");
        System.out.println("Regular message posted.");

        // 4. Post AI-triggered message
        roomMessageService.saveMessageAndNotifyAI(room.getId(), user.getId(), "What is the quadratic formula?");
        System.out.println("AI-triggered message posted.");
    }
}

