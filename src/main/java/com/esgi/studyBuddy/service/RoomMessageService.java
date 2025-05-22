package com.esgi.studyBuddy.service;

import com.esgi.studyBuddy.model.Room;
import com.esgi.studyBuddy.model.RoomMessage;
import com.esgi.studyBuddy.repository.RoomMessageRepository;
import com.esgi.studyBuddy.repository.RoomRepository;
import com.esgi.studyBuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RoomMessageService {
    private final RoomMessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public List<RoomMessage> getLastMessages(UUID roomId) {
        return messageRepository.findTop50ByRoom_IdOrderByCreatedAtDesc(roomId);
    }

    public void saveMessage(UUID roomId, RoomMessage message) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        message.setRoom(room);
        messageRepository.save(message);
    }
}
