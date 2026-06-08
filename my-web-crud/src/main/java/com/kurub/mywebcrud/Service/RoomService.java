package com.kurub.mywebcrud.Service;

import com.kurub.mywebcrud.Model.Room;
import com.kurub.mywebcrud.Repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public List<Room> getAllRooms() {
        return repository.findAll();
    }

    public void saveRoom(Room room) {
        repository.save(room);
    }

    public Room getRoomById(Long id) {
        Optional<Room> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Room not found for id :: " + id);
        }
    }

    public void deleteRoomById(Long id) {
        repository.deleteById(id);
    }
}
