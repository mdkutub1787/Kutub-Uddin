package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.Grade;
import com.kurub.mywebcrud.Model.Room;
import com.kurub.mywebcrud.Service.GradeService;
import com.kurub.mywebcrud.Service.RoomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/grades")
public class GradeController {

    private final GradeService gradeService;
    private final RoomService roomService;

    public GradeController(GradeService gradeService, RoomService roomService) {
        this.gradeService = gradeService;
        this.roomService = roomService;
    }

    @GetMapping
    public String viewGradesPage(Model model) {
        List<Grade> listGrades = gradeService.getAllGrades();
        List<Room> allRooms = roomService.getAllRooms();
        model.addAttribute("grades", listGrades);
        model.addAttribute("allRooms", allRooms);
        if (!model.containsAttribute("grade")) {
            model.addAttribute("grade", new Grade());
        }
        return "grades";
    }

    @PostMapping("/save")
    public String saveGrade(@Valid @ModelAttribute("grade") Grade grade, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.grade", bindingResult);
            redirectAttributes.addFlashAttribute("grade", grade);
            redirectAttributes.addFlashAttribute("message", "Validation Error!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/grades";
        }
        try {
            gradeService.saveGrade(grade);
            redirectAttributes.addFlashAttribute("message", "Success!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Duplicate name or invalid data.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("grade", grade);
        }
        return "redirect:/grades";
    }

    @GetMapping("/delete")
    public String deleteGrade(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            gradeService.deleteGradeById(id);
            redirectAttributes.addFlashAttribute("message", "Grade deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Cannot delete grade.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/grades";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id") Long id, Model model) {
        Grade grade = gradeService.getGradeById(id);
        model.addAttribute("grade", grade);
        return viewGradesPage(model);
    }
}
