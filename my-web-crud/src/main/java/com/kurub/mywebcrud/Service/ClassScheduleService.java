package com.kurub.mywebcrud.Service;

import com.kurub.mywebcrud.Model.ClassSchedule;
import com.kurub.mywebcrud.Repository.ClassScheduleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClassScheduleService {

    private final ClassScheduleRepository repository;

    public ClassScheduleService(ClassScheduleRepository repository) {
        this.repository = repository;
    }

    public List<ClassSchedule> getAllSchedules() {
        return repository.findAll();
    }

    public void saveSchedule(ClassSchedule schedule) {
        repository.save(schedule);
    }

    public ClassSchedule getScheduleById(Long id) {
        Optional<ClassSchedule> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Class Schedule not found for id :: " + id);
        }
    }

    public void deleteScheduleById(Long id) {
        repository.deleteById(id);
    }

    public List<ClassSchedule> getSchedulesByGradeId(Long gradeId) {
        return repository.findByGradeId(gradeId);
    }

    public List<ClassSchedule> getSchedulesByTeacherId(Long teacherId) {
        return repository.findByTeacherId(teacherId);
    }
}
