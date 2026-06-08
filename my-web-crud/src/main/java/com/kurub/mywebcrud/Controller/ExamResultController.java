package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.ExamResult;
import com.kurub.mywebcrud.Service.ExamResultService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/results")
public class ExamResultController {

    private final ExamResultService examResultService;

    public ExamResultController(ExamResultService examResultService) {
        this.examResultService = examResultService;
    }

    @PostMapping("/save")
    public String saveResult(@Valid @ModelAttribute("examResult") ExamResult examResult, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.examResult", bindingResult);
            redirectAttributes.addFlashAttribute("examResult", examResult);
            redirectAttributes.addFlashAttribute("message", "Validation Error: Please check exam result details.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/?view=results";
        }
        try {
            examResultService.saveResult(examResult);
            redirectAttributes.addFlashAttribute("message", "Success! Exam Result record updated.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Duplicate exam result or invalid data.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("examResult", examResult);
        }
        return "redirect:/?view=results";
    }

    @GetMapping("/delete")
    public String deleteResult(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            examResultService.deleteResultById(id);
            redirectAttributes.addFlashAttribute("message", "Exam Result record deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        } catch (Exception e) {
             redirectAttributes.addFlashAttribute("message", "Error: Cannot delete exam result.");
             redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=results";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            ExamResult examResult = examResultService.getResultById(id);
            redirectAttributes.addFlashAttribute("examResult", examResult);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Exam Result record not found for editing!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=results&id=" + id;
    }
}
