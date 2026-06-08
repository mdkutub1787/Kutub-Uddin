package com.kurub.mywebcrud.Service;

import com.kurub.mywebcrud.Model.ExamResult;
import com.kurub.mywebcrud.Repository.ExamResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamResultService {

    private final ExamResultRepository repository;

    public ExamResultService(ExamResultRepository repository) {
        this.repository = repository;
    }

    public List<ExamResult> getAllResults() {
        return repository.findAll();
    }

    public void saveResult(ExamResult result) {
        // Simple logic for grade letter based on marks
        if (result.getMarks() != null) {
            double m = result.getMarks();
            if (m >= 80) result.setGradeLetter("A+");
            else if (m >= 70) result.setGradeLetter("A");
            else if (m >= 60) result.setGradeLetter("B");
            else if (m >= 50) result.setGradeLetter("C");
            else if (m >= 40) result.setGradeLetter("D");
            else result.setGradeLetter("F");
        }
        repository.save(result);
    }

    public ExamResult getResultById(Long id) {
        Optional<ExamResult> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Exam Result not found for id :: " + id);
        }
    }

    public void deleteResultById(Long id) {
        repository.deleteById(id);
    }
}
