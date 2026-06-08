package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.*;
import com.kurub.mywebcrud.Service.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashboardController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final GradeService gradeService;
    private final SubjectService subjectService;
    private final AttendanceService attendanceService;
    private final ExamResultService examResultService;
    private final FeeRecordService feeRecordService;
    private final DepartmentService departmentService;
    private final RoomService roomService;
    private final AcademicYearService academicYearService;
    private final ClassScheduleService scheduleService;

    public DashboardController(StudentService studentService, TeacherService teacherService,
                               GradeService gradeService, SubjectService subjectService,
                               AttendanceService attendanceService, ExamResultService examResultService,
                               FeeRecordService feeRecordService, DepartmentService departmentService,
                               RoomService roomService, AcademicYearService academicYearService,
                               ClassScheduleService scheduleService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.gradeService = gradeService;
        this.subjectService = subjectService;
        this.attendanceService = attendanceService;
        this.examResultService = examResultService;
        this.feeRecordService = feeRecordService;
        this.departmentService = departmentService;
        this.roomService = roomService;
        this.academicYearService = academicYearService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/")
    public String viewDashboard(Model model,
                               @RequestParam(value = "view", defaultValue = "overview") String view,
                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                               @RequestParam(value = "sortField", defaultValue = "firstName") String sortField,
                               @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "id", required = false) Long editId) {

        // Stats
        model.addAttribute("totalStudents", studentService.getAllStudents().size());
        model.addAttribute("totalTeachers", teacherService.getAllTeachers().size());
        model.addAttribute("totalGrades", gradeService.getAllGrades().size());
        model.addAttribute("totalSubjects", subjectService.getAllSubjects().size());
        model.addAttribute("totalDepartments", departmentService.getAllDepartments().size());
        model.addAttribute("totalRooms", roomService.getAllRooms().size());

        model.addAttribute("activeView", view);

        // Always provide these lists to avoid Thymeleaf resolution errors
        model.addAttribute("allStudents", studentService.getAllStudents());
        model.addAttribute("allGrades", gradeService.getAllGrades());
        model.addAttribute("allSubjects", subjectService.getAllSubjects());
        model.addAttribute("allTeachers", teacherService.getAllTeachers());
        model.addAttribute("allDepartments", departmentService.getAllDepartments());
        model.addAttribute("allRooms", roomService.getAllRooms());
        model.addAttribute("allAcademicYears", academicYearService.getAllAcademicYears());
        
        // Populate specific lists based on view (if different from 'all' lists)
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("grades", gradeService.getAllGrades());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("academicYears", academicYearService.getAllAcademicYears());
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        model.addAttribute("attendanceRecords", attendanceService.getAllAttendance());
        model.addAttribute("examResults", examResultService.getAllResults());
        model.addAttribute("feeRecords", feeRecordService.getAllFeeRecords());

        // Default objects for forms
        if (!model.containsAttribute("student")) model.addAttribute("student", new Student());
        if (!model.containsAttribute("teacher")) model.addAttribute("teacher", new Teacher());
        if (!model.containsAttribute("department")) model.addAttribute("department", new Department());
        if (!model.containsAttribute("grade")) model.addAttribute("grade", new Grade());
        if (!model.containsAttribute("subject")) model.addAttribute("subject", new Subject());
        if (!model.containsAttribute("room")) model.addAttribute("room", new Room());
        if (!model.containsAttribute("academicYear")) model.addAttribute("academicYear", new AcademicYear());
        if (!model.containsAttribute("schedule")) model.addAttribute("schedule", new ClassSchedule());
        if (!model.containsAttribute("attendance")) model.addAttribute("attendance", new Attendance());
        if (!model.containsAttribute("examResult")) model.addAttribute("examResult", new ExamResult());
        if (!model.containsAttribute("feeRecord")) model.addAttribute("feeRecord", new FeeRecord());

        if (view.equals("students")) {
            Page<Student> page = studentService.findPaginated(pageNo, 5, sortField, sortDir, keyword);
            model.addAttribute("students", page.getContent());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("keyword", keyword);
            
            if (editId != null) model.addAttribute("student", studentService.getStudentById(editId));
        } 
        else if (view.equals("teachers") && editId != null) {
            model.addAttribute("teacher", teacherService.getTeacherById(editId));
        } 
        else if (view.equals("grades") && editId != null) {
            model.addAttribute("grade", gradeService.getGradeById(editId));
        }
        else if (view.equals("departments") && editId != null) {
            model.addAttribute("department", departmentService.getDepartmentById(editId));
        }
        else if (view.equals("rooms") && editId != null) {
            model.addAttribute("room", roomService.getRoomById(editId));
        }
        else if (view.equals("academic-years") && editId != null) {
            model.addAttribute("academicYear", academicYearService.getAcademicYearById(editId));
        }
        else if (view.equals("attendance") && editId != null) {
            model.addAttribute("attendance", attendanceService.getAttendanceById(editId));
        }
        else if (view.equals("results") && editId != null) {
            model.addAttribute("examResult", examResultService.getResultById(editId));
        }
        else if (view.equals("fees") && editId != null) {
            model.addAttribute("feeRecord", feeRecordService.getFeeRecordById(editId));
        }

        return "dashboard";
    }
}
