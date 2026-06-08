package com.kurub.mywebcrud.Service;

import com.kurub.mywebcrud.Model.Attendance;
import com.kurub.mywebcrud.Repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRepository repository;

    public AttendanceService(AttendanceRepository repository) {
        this.repository = repository;
    }

    public List<Attendance> getAllAttendance() {
        return repository.findAll();
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return repository.findByDate(date);
    }

    public void saveAttendance(Attendance attendance) {
        repository.save(attendance);
    }

    public Attendance getAttendanceById(Long id) {
        Optional<Attendance> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Attendance record not found for id :: " + id);
        }
    }

    public void deleteAttendanceById(Long id) {
        repository.deleteById(id);
    }
}
