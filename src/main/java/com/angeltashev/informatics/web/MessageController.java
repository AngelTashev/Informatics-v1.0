package com.angeltashev.informatics.web;

import com.angeltashev.informatics.messages.model.binding.MessageAddBindingModel;
import com.angeltashev.informatics.messages.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.angeltashev.informatics.constants.ApplicationParameters.BINDING_RESULT_PATH;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/contact-us")
public class MessageController {

    private final MessageService messageService;

    @PreAuthorize("permitAll()")
    @GetMapping
    public String getMessageForm(Model model) {
        if (!model.containsAttribute("messageModel")) {
            model.addAttribute("messageModel", new MessageAddBindingModel());
        }
        return "/message/message-add";
    }

    @PreAuthorize("permitAll()")
    @PostMapping
    public String confirmMessageForm(@Valid @ModelAttribute("messageModel") MessageAddBindingModel messageModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.error("Confirm message form: Binding model contains errors");
            redirectAttributes.addFlashAttribute("messageModel", messageModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + "messageModel", bindingResult);
            return "redirect:/contact-us";
        }
        this.messageService.addMessage(messageModel);
        log.info("Confirm message form: Successfully sent message by " + messageModel.getFullName() + " with email " + messageModel.getEmail());
        return "redirect:/";
    }
}
