package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.Attendance;
import com.kurub.mywebcrud.Service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/save")
    public String saveAttendance(@Valid @ModelAttribute("attendance") Attendance attendance, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.attendance", bindingResult);
            redirectAttributes.addFlashAttribute("attendance", attendance);
            redirectAttributes.addFlashAttribute("message", "Validation Error: Please check attendance details.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/?view=attendance";
        }
        try {
            attendanceService.saveAttendance(attendance);
            redirectAttributes.addFlashAttribute("message", "Success! Attendance record updated.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Duplicate attendance for same student on this date.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("attendance", attendance);
        }
        return "redirect:/?view=attendance";
    }

    @GetMapping("/delete")
    public String deleteAttendance(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            attendanceService.deleteAttendanceById(id);
            redirectAttributes.addFlashAttribute("message", "Attendance record deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        } catch (Exception e) {
             redirectAttributes.addFlashAttribute("message", "Error: Cannot delete attendance record.");
             redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=attendance";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Attendance attendance = attendanceService.getAttendanceById(id);
            redirectAttributes.addFlashAttribute("attendance", attendance);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Attendance record not found for editing!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=attendance&id=" + id;
    }
}
