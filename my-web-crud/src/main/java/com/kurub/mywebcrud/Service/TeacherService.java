package com.kurub.mywebcrud.Service;

import com.kurub.mywebcrud.Model.Teacher;
import com.kurub.mywebcrud.Repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository repository;

    public TeacherService(TeacherRepository repository) {
        this.repository = repository;
    }

    public List<Teacher> getAllTeachers() {
        return repository.findAll();
    }

    public Page<Teacher> findPaginated(int pageNo, int pageSize, String sortField, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return repository.search(keyword, pageable);
        }
        return repository.findAll(pageable);
    }

    public void saveTeacher(Teacher teacher) {
        repository.save(teacher);
    }

    public Teacher getTeacherById(Long id) {
        Optional<Teacher> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Teacher not found for id :: " + id);
        }
    }

    public void deleteTeacherById(Long id) {
        repository.deleteById(id);
    }
}
