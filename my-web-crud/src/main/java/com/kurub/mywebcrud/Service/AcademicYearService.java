package com.kurub.mywebcrud.Service;

import com.kurub.mywebcrud.Model.AcademicYear;
import com.kurub.mywebcrud.Repository.AcademicYearRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AcademicYearService {

    private final AcademicYearRepository repository;

    public AcademicYearService(AcademicYearRepository repository) {
        this.repository = repository;
    }

    public List<AcademicYear> getAllAcademicYears() {
        return repository.findAll();
    }

    public void saveAcademicYear(AcademicYear year) {
        repository.save(year);
    }

    public AcademicYear getAcademicYearById(Long id) {
        Optional<AcademicYear> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Academic Year not found for id :: " + id);
        }
    }

    public void deleteAcademicYearById(Long id) {
        repository.deleteById(id);
    }
}
