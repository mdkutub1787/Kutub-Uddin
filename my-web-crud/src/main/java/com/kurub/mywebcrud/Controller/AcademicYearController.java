package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.AcademicYear;
import com.kurub.mywebcrud.Service.AcademicYearService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/academic-years")
public class AcademicYearController {

    private final AcademicYearService academicYearService;

    public AcademicYearController(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @PostMapping("/save")
    public String saveAcademicYear(@Valid @ModelAttribute("academicYear") AcademicYear academicYear, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.academicYear", bindingResult);
            redirectAttributes.addFlashAttribute("academicYear", academicYear);
            redirectAttributes.addFlashAttribute("message", "Validation Error: Please check your input for Academic Year.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/?view=academic-years";
        }
        try {
            academicYearService.saveAcademicYear(academicYear);
            redirectAttributes.addFlashAttribute("message", "Success! Academic Year record updated.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Duplicate Academic Year name.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("academicYear", academicYear);
        }
        return "redirect:/?view=academic-years";
    }

    @GetMapping("/delete")
    public String deleteAcademicYear(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            academicYearService.deleteAcademicYearById(id);
            redirectAttributes.addFlashAttribute("message", "Academic Year record deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        } catch (Exception e) {
             redirectAttributes.addFlashAttribute("message", "Error: Cannot delete Academic Year.");
             redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=academic-years";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            AcademicYear academicYear = academicYearService.getAcademicYearById(id);
            redirectAttributes.addFlashAttribute("academicYear", academicYear);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Academic Year not found for editing!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=academic-years&id=" + id;
    }
}
