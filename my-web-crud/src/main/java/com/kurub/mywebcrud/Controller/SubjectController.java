package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.Subject;
import com.kurub.mywebcrud.Service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public String viewSubjectsPage(Model model) {
        List<Subject> listSubjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", listSubjects);
        if (!model.containsAttribute("subject")) {
            model.addAttribute("subject", new Subject());
        }
        return "subjects";
    }

    @PostMapping("/save")
    public String saveSubject(@ModelAttribute("subject") Subject subject, RedirectAttributes redirectAttributes) {
        try {
            subjectService.saveSubject(subject);
            redirectAttributes.addFlashAttribute("message", "Subject saved successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Subject name must be unique!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("subject", subject);
        }
        return "redirect:/subjects";
    }

    @GetMapping("/delete/{id}")
    public String deleteSubject(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            subjectService.deleteSubjectById(id);
            redirectAttributes.addFlashAttribute("message", "Subject deleted successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Cannot delete subject.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/subjects";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable(name = "id") Long id, Model model) {
        Subject subject = subjectService.getSubjectById(id);
        model.addAttribute("subject", subject);
        return viewSubjectsPage(model);
    }
}
