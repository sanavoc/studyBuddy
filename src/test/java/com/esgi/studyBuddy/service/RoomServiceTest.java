package com.esgi.studyBuddy.service;

import com.esgi.studyBuddy.DTO.RoomDurationUpdateRequest;
import com.esgi.studyBuddy.model.*;
import com.esgi.studyBuddy.repository.RoomMemberRepository;
import com.esgi.studyBuddy.repository.RoomRepository;
import com.esgi.studyBuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomMemberRepository roomMemberRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRoom_shouldSaveRoomAndAddOwnerAsMember() {
        User owner = new User();
        owner.setId(UUID.randomUUID());

        Room room = new Room();
        room.setOwner(owner);
        room.setId(UUID.randomUUID());

        when(roomRepository.save(room)).thenReturn(room);
        when(roomMemberRepository.save(any(RoomMember.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UUID result = roomService.createRoom(room);

        assertEquals(room.getId(), result);
        verify(roomRepository).save(room);
        verify(roomMemberRepository).save(argThat(member ->
                member.getRoom().equals(room) &&
                        member.getUser().equals(owner) &&
                        member.getRole() == UserRole.owner));
    }

    @Test
    void joinRoom_shouldSaveMemberIfNotAlreadyMember() {
        UUID roomId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Room room = new Room();
        room.setId(roomId);

        User user = new User();
        user.setId(userId);

        RoomMemberId memberId = new RoomMemberId(userId, roomId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roomMemberRepository.existsById(memberId)).thenReturn(false);
        when(roomMemberRepository.save(any(RoomMember.class))).thenAnswer(invocation -> invocation.getArgument(0));

        roomService.joinRoom(roomId, userId);

        verify(roomRepository).findById(roomId);
        verify(userRepository).findById(userId);
        verify(roomMemberRepository).existsById(memberId);
        verify(roomMemberRepository).save(argThat(member ->
                member.getRoom().equals(room) &&
                        member.getUser().equals(user) &&
                        member.getRole() == UserRole.member));
    }

    @Test
    void joinRoom_shouldNotSaveIfMemberAlreadyExists() {
        UUID roomId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Room room = new Room();
        room.setId(roomId);

        User user = new User();
        user.setId(userId);

        RoomMemberId memberId = new RoomMemberId(userId, roomId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roomMemberRepository.existsById(memberId)).thenReturn(true);

        roomService.joinRoom(roomId, userId);

        verify(roomMemberRepository).existsById(memberId);
        verify(roomMemberRepository, never()).save(any());
    }


//    @Test
//    void updateTheme_shouldUpdateRoomTheme() {
//        UUID roomId = UUID.randomUUID();
//        Room room = new Room();
//        room.setId(roomId);
//
//        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
//        when(roomRepository.save(room)).thenReturn(room);
//
//        String newTheme = "dark-mode";
//
//        roomService.updateTheme(roomId, newTheme);
//
//        assertEquals(newTheme, room.getThemeConfig());
//        verify(roomRepository).findById(roomId);
//        verify(roomRepository).save(room);
//    }

    @Test
    void updateDurations_shouldUpdateFocusAndBreakDurations() {
        UUID roomId = UUID.randomUUID();
        Room room = new Room();
        room.setId(roomId);
        room.setFocusDuration(25);
        room.setBreakDuration(5);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomRepository.save(room)).thenReturn(room);

        RoomDurationUpdateRequest request = new RoomDurationUpdateRequest();
        request.setFocusDuration(30);
        request.setBreakDuration(10);

        roomService.updateDurations(roomId, request);

        assertEquals(30, room.getFocusDuration());
        assertEquals(10, room.getBreakDuration());
        verify(roomRepository).findById(roomId);
        verify(roomRepository).save(room);
    }

    @Test
    void updateDurations_shouldOnlyUpdateNonNullDurations() {
        UUID roomId = UUID.randomUUID();
        Room room = new Room();
        room.setId(roomId);
        room.setFocusDuration(25);
        room.setBreakDuration(5);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomRepository.save(room)).thenReturn(room);

        RoomDurationUpdateRequest request = new RoomDurationUpdateRequest();
        request.setFocusDuration(null);
        request.setBreakDuration(15);

        roomService.updateDurations(roomId, request);

        assertEquals(25, room.getFocusDuration());
        assertEquals(15, room.getBreakDuration());
        verify(roomRepository).findById(roomId);
        verify(roomRepository).save(room);
    }
}
