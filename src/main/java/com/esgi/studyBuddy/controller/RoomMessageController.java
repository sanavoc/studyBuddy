package com.esgi.studyBuddy.controller;

import com.esgi.studyBuddy.model.RoomMessage;
import com.esgi.studyBuddy.service.RoomMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class RoomMessageController {
    private final RoomMessageService messageService;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<RoomMessage>> getMessages(@PathVariable UUID roomId) {
        return ResponseEntity.ok(messageService.getLastMessages(roomId));
    }

    @PostMapping("/room/{roomId}")
    @SendTo("/topic/messages")
    public ResponseEntity<Void> postMessage(@PathVariable UUID roomId, @RequestBody RoomMessage message) {
        messageService.saveMessage(roomId, message);
        return ResponseEntity.ok().build();
    }
}
