package com.kurub.mywebcrud.Service;

import com.kurub.mywebcrud.Model.Department;
import com.kurub.mywebcrud.Repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public List<Department> getAllDepartments() {
        return repository.findAll();
    }

    public void saveDepartment(Department department) {
        repository.save(department);
    }

    public Department getDepartmentById(Long id) {
        Optional<Department> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Department not found for id :: " + id);
        }
    }

    public void deleteDepartmentById(Long id) {
        repository.deleteById(id);
    }
}
