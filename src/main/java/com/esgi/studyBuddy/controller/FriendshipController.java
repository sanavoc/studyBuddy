package com.esgi.studyBuddy.controller;

import com.esgi.studyBuddy.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;


@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;

    @PostMapping("/request")
    public ResponseEntity<Void> sendRequest(@RequestParam UUID from, @RequestParam UUID to) {
        friendshipService.sendRequest(from, to);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptRequest(@RequestParam UUID from, @RequestParam UUID to) {
        friendshipService.acceptRequest(from, to);
        return ResponseEntity.ok().build();
    }
}
