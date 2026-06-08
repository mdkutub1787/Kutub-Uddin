package com.kurub.mywebcrud.Controller;

import com.kurub.mywebcrud.Model.FeeRecord;
import com.kurub.mywebcrud.Service.FeeRecordService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fees")
public class FeeRecordController {

    private final FeeRecordService feeRecordService;

    public FeeRecordController(FeeRecordService feeRecordService) {
        this.feeRecordService = feeRecordService;
    }

    @PostMapping("/save")
    public String saveFeeRecord(@Valid @ModelAttribute("feeRecord") FeeRecord feeRecord, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.feeRecord", bindingResult);
            redirectAttributes.addFlashAttribute("feeRecord", feeRecord);
            redirectAttributes.addFlashAttribute("message", "Validation Error: Please check fee details.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/?view=fees";
        }
        try {
            feeRecordService.saveFeeRecord(feeRecord);
            redirectAttributes.addFlashAttribute("message", "Success! Fee Record record updated.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Duplicate fee record or invalid data.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("feeRecord", feeRecord);
        }
        return "redirect:/?view=fees";
    }

    @GetMapping("/delete")
    public String deleteFeeRecord(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            feeRecordService.deleteFeeRecordById(id);
            redirectAttributes.addFlashAttribute("message", "Fee Record record deleted.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        } catch (Exception e) {
             redirectAttributes.addFlashAttribute("message", "Error: Cannot delete fee record.");
             redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=fees";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            FeeRecord feeRecord = feeRecordService.getFeeRecordById(id);
            redirectAttributes.addFlashAttribute("feeRecord", feeRecord);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Fee Record not found for editing!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/?view=fees&id=" + id;
    }
}
