package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.ClassSchedule;
import com.kurub.mywebcrud.Service.ClassScheduleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/schedules")
public class ClassScheduleController {

    private final ClassScheduleService scheduleService;

    public ClassScheduleController(ClassScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/save")
    public String saveSchedule(@Valid @ModelAttribute("schedule") ClassSchedule schedule, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.schedule", bindingResult);
            redirectAttributes.addFlashAttribute("schedule", schedule);
            redirectAttributes.addFlashAttribute("message", "Validation Error: Please check your input for Class Schedule.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/?view=schedules";
        }
        try {
            scheduleService.saveSchedule(schedule);
            redirectAttributes.addFlashAttribute("message", "Success! Class Schedule record updated.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Overlapping schedule or invalid data.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("schedule", schedule);
        }
        return "redirect:/?view=schedules";
    }

    @GetMapping("/delete")
    public String deleteSchedule(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            scheduleService.deleteScheduleById(id);
            redirectAttributes.addFlashAttribute("message", "Class Schedule record deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        } catch (Exception e) {
             redirectAttributes.addFlashAttribute("message", "Error: Cannot delete Class Schedule.");
             redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=schedules";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            ClassSchedule schedule = scheduleService.getScheduleById(id);
            redirectAttributes.addFlashAttribute("schedule", schedule);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Class Schedule not found for editing!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=schedules&id=" + id;
    }
}
