package com.kurub.mywebcrud.Service;

import com.kurub.mywebcrud.Model.Subject;
import com.kurub.mywebcrud.Repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository repository;

    public SubjectService(SubjectRepository repository) {
        this.repository = repository;
    }

    public List<Subject> getAllSubjects() {
        return repository.findAll();
    }

    public void saveSubject(Subject subject) {
        repository.save(subject);
    }

    public Subject getSubjectById(Long id) {
        Optional<Subject> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Subject not found for id :: " + id);
        }
    }

    public void deleteSubjectById(Long id) {
        repository.deleteById(id);
    }
}
