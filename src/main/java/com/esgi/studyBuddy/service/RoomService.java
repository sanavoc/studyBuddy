package com.esgi.studyBuddy.service;

import com.esgi.studyBuddy.DTO.RoomDurationUpdateRequest;
import com.esgi.studyBuddy.model.*;
import com.esgi.studyBuddy.repository.RoomMemberRepository;
import com.esgi.studyBuddy.repository.RoomRepository;
import com.esgi.studyBuddy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomMemberRepository roomMemberRepository;

    @Transactional
    public UUID createRoom(Room room) {
        Room saved = roomRepository.save(room);
        RoomMember member = RoomMember.builder()
                .room(saved)
                .user(saved.getOwner())
                .userId(saved.getOwner().getId())
                .roomId(saved.getId())
                .role(UserRole.owner)
                .build();
        roomMemberRepository.save(member);
        return saved.getId();
    }

    @Transactional
    public void joinRoom(UUID roomId, UUID userId) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        RoomMemberId id = new RoomMemberId(userId, roomId);
        if (roomMemberRepository.existsById(id)) return;
        roomMemberRepository.save(RoomMember.builder()
                .room(room)
                .user(user)
                .userId(userId)
                .roomId(roomId)
                .role(UserRole.member)
                .build());
    }

    @Transactional
    public void updateTheme(UUID roomId, String themeConfig) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        room.setThemeConfig(themeConfig);
        roomRepository.save(room);
    }

    @Transactional
    public void updateDurations(UUID roomId, RoomDurationUpdateRequest request) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        if (request.getFocusDuration() != null) room.setFocusDuration(request.getFocusDuration());
        if (request.getBreakDuration() != null) room.setBreakDuration(request.getBreakDuration());
        roomRepository.save(room);
    }
}
