package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.Teacher;
import com.kurub.mywebcrud.Model.Department;
import com.kurub.mywebcrud.Service.TeacherService;
import com.kurub.mywebcrud.Service.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final DepartmentService departmentService;

    public TeacherController(TeacherService teacherService, DepartmentService departmentService) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String viewTeachersPage(Model model,
                                   @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                   @RequestParam(value = "sortField", defaultValue = "firstName") String sortField,
                                   @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                                   @RequestParam(value = "keyword", required = false) String keyword) {
        
        int pageSize = 5;
        Page<Teacher> page = teacherService.findPaginated(pageNo, pageSize, sortField, sortDir, keyword);
        
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("teachers", page.getContent());
        model.addAttribute("allDepartments", departmentService.getAllDepartments());
        
        if (!model.containsAttribute("teacher")) {
            model.addAttribute("teacher", new Teacher());
        }

        return "teachers";
    }

    @PostMapping("/save")
    public String saveTeacher(@ModelAttribute("teacher") Teacher teacher, RedirectAttributes redirectAttributes) {
        try {
            teacherService.saveTeacher(teacher);
            redirectAttributes.addFlashAttribute("message", "Teacher saved successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error saving teacher!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/teachers";
    }

    @GetMapping("/delete")
    public String deleteTeacher(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            teacherService.deleteTeacherById(id);
            redirectAttributes.addFlashAttribute("message", "Teacher deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting teacher.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/teachers";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id") Long id, Model model) {
        Teacher teacher = teacherService.getTeacherById(id);
        model.addAttribute("teacher", teacher);
        return viewTeachersPage(model, 1, "firstName", "asc", null);
    }
}
