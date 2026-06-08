package com.kurub.mywebcrud.Repository;

import com.kurub.mywebcrud.Model.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
    List<ClassSchedule> findByGradeId(Long gradeId);
    List<ClassSchedule> findByTeacherId(Long teacherId);
    List<ClassSchedule> findByRoomId(Long roomId);
    List<ClassSchedule> findByDayOfWeek(String dayOfWeek);
}
