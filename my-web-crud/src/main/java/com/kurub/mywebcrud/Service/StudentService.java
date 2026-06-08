package com.kurub.mywebcrud.Service;

import com.kurub.mywebcrud.Model.Student;
import com.kurub.mywebcrud.Repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Page<Student> findPaginated(int pageNo, int pageSize, String sortField, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return repository.search(keyword, pageable);
        }
        return repository.findAll(pageable);
    }

    public void saveStudent(Student student) {
        // Dynamic Roll Number Generation
        if (student.getId() == null && (student.getRollNumber() == null || student.getRollNumber().isEmpty())) {
            student.setRollNumber(generateRollNumber());
        }
        
        // Safety check for grade
        if (student.getGrade() != null && student.getGrade().getId() == null) {
            student.setGrade(null);
        }
        
        repository.save(student);
    }

    private String generateRollNumber() {
        Optional<String> lastRoll = repository.findLastRollNumber();
        int nextId = 1;
        
        if (lastRoll.isPresent()) {
            try {
                String numericPart = lastRoll.get().replace("STD-", "");
                nextId = Integer.parseInt(numericPart) + 1;
            } catch (Exception e) {
                nextId = (int) repository.count() + 1;
            }
        }
        
        return String.format("STD-%04d", nextId);
    }

    public Student getStudentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found for id :: " + id));
    }

    public void deleteStudentById(Long id) {
        repository.deleteById(id);
    }
}
