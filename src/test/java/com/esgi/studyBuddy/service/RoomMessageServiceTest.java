package com.esgi.studyBuddy.service;

import com.esgi.studyBuddy.model.Room;
import com.esgi.studyBuddy.model.RoomMessage;
import com.esgi.studyBuddy.repository.RoomMessageRepository;
import com.esgi.studyBuddy.repository.RoomRepository;
import com.esgi.studyBuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RoomMessageServiceTest {

    @Mock
    private RoomMessageRepository messageRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoomMessageService roomMessageService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLastMessages_shouldReturnTop50Messages() {
        UUID roomId = UUID.randomUUID();
        RoomMessage msg1 = new RoomMessage();
        RoomMessage msg2 = new RoomMessage();
        List<RoomMessage> messages = List.of(msg1, msg2);

        when(messageRepository.findTop50ByRoom_IdOrderByCreatedAtDesc(roomId)).thenReturn(messages);

        List<RoomMessage> result = roomMessageService.getLastMessages(roomId);

        assertEquals(messages, result);
        verify(messageRepository).findTop50ByRoom_IdOrderByCreatedAtDesc(roomId);
    }

    @Test
    void saveMessage_shouldSetRoomAndSave() {
        UUID roomId = UUID.randomUUID();
        Room room = new Room();
        room.setId(roomId);

        RoomMessage message = new RoomMessage();

        when(roomRepository.findById(roomId)).thenReturn(java.util.Optional.of(room));
        when(messageRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        roomMessageService.saveMessage(roomId, message);

        assertEquals(room, message.getRoom());
        verify(roomRepository).findById(roomId);
        verify(messageRepository).save(message);
    }

    @Test
    void saveMessage_shouldThrowIfRoomNotFound() {
        UUID roomId = UUID.randomUUID();
        RoomMessage message = new RoomMessage();

        when(roomRepository.findById(roomId)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> roomMessageService.saveMessage(roomId, message));

        verify(roomRepository).findById(roomId);
        verify(messageRepository, never()).save(any());
    }
}
