package com.kurub.mywebcrud.Service;

import com.kurub.mywebcrud.Model.FeeRecord;
import com.kurub.mywebcrud.Repository.FeeRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeeRecordService {

    private final FeeRecordRepository repository;

    public FeeRecordService(FeeRecordRepository repository) {
        this.repository = repository;
    }

    public List<FeeRecord> getAllFeeRecords() {
        return repository.findAll();
    }

    public void saveFeeRecord(FeeRecord feeRecord) {
        repository.save(feeRecord);
    }

    public FeeRecord getFeeRecordById(Long id) {
        Optional<FeeRecord> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Fee Record not found for id :: " + id);
        }
    }

    public void deleteFeeRecordById(Long id) {
        repository.deleteById(id);
    }
}
