package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.Student;
import com.kurub.mywebcrud.Service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // This controller will only handle POST and DELETE actions,
    // GET requests for viewing students will be handled by DashboardController
    
    @PostMapping("/students/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.student", bindingResult);
            redirectAttributes.addFlashAttribute("student", student);
            redirectAttributes.addFlashAttribute("message", "Validation Error: Please check your input for Student.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/?view=students";
        }
        try {
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("message", "Success! Student record updated.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Duplicate Email/Roll or invalid Grade.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("student", student);
        }
        return "redirect:/?view=students";
    }

    @GetMapping("/students/delete") // Changed to @GetMapping for simplicity with redirect
    public String deleteStudent(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteStudentById(id);
        redirectAttributes.addFlashAttribute("message", "Student record deleted.");
        redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        return "redirect:/?view=students";
    }

    @GetMapping("/students/edit") // Changed to @GetMapping for simplicity with redirect
    public String showEditForm(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Student student = studentService.getStudentById(id);
            redirectAttributes.addFlashAttribute("student", student);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Student not found for editing!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=students";
    }
}
