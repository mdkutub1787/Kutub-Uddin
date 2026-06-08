package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.Room;
import com.kurub.mywebcrud.Service.RoomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/save")
    public String saveRoom(@Valid @ModelAttribute("room") Room room, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.room", bindingResult);
            redirectAttributes.addFlashAttribute("room", room);
            redirectAttributes.addFlashAttribute("message", "Validation Error: Please check your input for Room.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/?view=rooms";
        }
        try {
            roomService.saveRoom(room);
            redirectAttributes.addFlashAttribute("message", "Success! Room record updated.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Duplicate Room name.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("room", room);
        }
        return "redirect:/?view=rooms";
    }

    @GetMapping("/delete")
    public String deleteRoom(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoomById(id);
            redirectAttributes.addFlashAttribute("message", "Room record deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        } catch (Exception e) {
             redirectAttributes.addFlashAttribute("message", "Error: Cannot delete Room.");
             redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=rooms";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Room room = roomService.getRoomById(id);
            redirectAttributes.addFlashAttribute("room", room);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Room not found for editing!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=rooms&id=" + id;
    }
}
