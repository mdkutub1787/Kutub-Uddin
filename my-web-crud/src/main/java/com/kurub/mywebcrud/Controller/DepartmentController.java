package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.Department;
import com.kurub.mywebcrud.Service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/save")
    public String saveDepartment(@Valid @ModelAttribute("department") Department department, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.department", bindingResult);
            redirectAttributes.addFlashAttribute("department", department);
            redirectAttributes.addFlashAttribute("message", "Validation Error: Please check your input for Department.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/?view=departments";
        }
        try {
            departmentService.saveDepartment(department);
            redirectAttributes.addFlashAttribute("message", "Success! Department record updated.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Duplicate Department name.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("department", department);
        }
        return "redirect:/?view=departments";
    }

    @GetMapping("/delete")
    public String deleteDepartment(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteDepartmentById(id);
            redirectAttributes.addFlashAttribute("message", "Department record deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        } catch (Exception e) {
             redirectAttributes.addFlashAttribute("message", "Error: Cannot delete Department.");
             redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=departments";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Department department = departmentService.getDepartmentById(id);
            redirectAttributes.addFlashAttribute("department", department);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Department not found for editing!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=departments&id=" + id;
    }
}
