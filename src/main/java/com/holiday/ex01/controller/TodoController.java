package com.holiday.ex01.controller;

import com.holiday.ex01.model.Todo;
import com.holiday.ex01.model.TodoPriority;
import com.holiday.ex01.model.TodoStatus;
import com.holiday.ex01.service.TodoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TodoController {
    private static final String OWNER_NAME_SESSION_KEY = "ownerName";
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/owner")
    public String showOwnerForm(HttpSession session, Model model) {
        Object ownerName = session.getAttribute(OWNER_NAME_SESSION_KEY);
        model.addAttribute("ownerName", ownerName == null ? "" : ownerName.toString());
        return "owner-form.html";
    }

    @PostMapping("/owner")
    public String saveOwnerName(@RequestParam String ownerName, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(ownerName)) {
            redirectAttributes.addFlashAttribute("message", "owner.message.required");
            return "redirect:/owner";
        }

        session.setAttribute(OWNER_NAME_SESSION_KEY, ownerName.trim());
        return "redirect:/";
    }

    @GetMapping("/")
    public String showList(Model model, HttpSession session) {
        String ownerName = getOwnerName(session);
        if (ownerName == null) {
            return "redirect:/owner";
        }

        model.addAttribute("ownerName", ownerName);
        model.addAttribute("todos", todoService.findAll());
        return "todo-list";
    }

    @GetMapping("/todos/new")
    public String showCreateForm(Model model, HttpSession session) {
        if (getOwnerName(session) == null) {
            return "redirect:/owner";
        }

        Todo todo = new Todo();
        todo.setStatus(TodoStatus.PENDING);
        todo.setPriority(TodoPriority.MEDIUM);
        model.addAttribute("todo", todo);
        return "todo-form";
    }

    @GetMapping("/todos/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        if (getOwnerName(session) == null) {
            return "redirect:/owner";
        }

        return todoService.findById(id)
                .map(todo -> {
                    model.addAttribute("todo", todo);
                    return "todo-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("message", "todo.message.editNotFound");
                    return "redirect:/";
                });
    }

    @PostMapping("/todos")
    public String saveTodo(@Valid @ModelAttribute("todo") Todo todo,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {
        if (getOwnerName(session) == null) {
            return "redirect:/owner";
        }

        if (bindingResult.hasErrors()) {
            return "todo-form";
        }

        boolean isUpdate = todo.getId() != null;
        todoService.save(todo);
        redirectAttributes.addFlashAttribute(
                "message",
                isUpdate ? "todo.message.updated" : "todo.message.created"
        );
        return "redirect:/";
    }

    @GetMapping("/todos/delete/{id}")
    public String deleteTodo(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (getOwnerName(session) == null) {
            return "redirect:/owner";
        }

        boolean deleted = todoService.deleteById(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "todo.message.deleted");
        } else {
            redirectAttributes.addFlashAttribute("message", "todo.message.deleteNotFound");
        }
        return "redirect:/";
    }

    private String getOwnerName(HttpSession session) {
        Object ownerName = session.getAttribute(OWNER_NAME_SESSION_KEY);
        if (ownerName == null) {
            return null;
        }

        String owner = ownerName.toString().trim();
        return StringUtils.hasText(owner) ? owner : null;
    }
}
