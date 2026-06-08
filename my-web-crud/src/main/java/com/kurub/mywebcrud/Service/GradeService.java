package com.kurub.mywebcrud.Service;

import com.kurub.mywebcrud.Model.Grade;
import com.kurub.mywebcrud.Repository.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    private final GradeRepository repository;

    public GradeService(GradeRepository repository) {
        this.repository = repository;
    }

    public List<Grade> getAllGrades() {
        return repository.findAll();
    }

    public void saveGrade(Grade grade) {
        repository.save(grade);
    }

    public Grade getGradeById(Long id) {
        Optional<Grade> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Grade not found for id :: " + id);
        }
    }

    public void deleteGradeById(Long id) {
        repository.deleteById(id);
    }
}
