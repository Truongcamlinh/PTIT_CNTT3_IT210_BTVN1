package com.holiday.ex01.controller;

import com.holiday.ex01.model.Todo;
import com.holiday.ex01.model.TodoPriority;
import com.holiday.ex01.model.TodoStatus;
import com.holiday.ex01.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String showList(Model model) {
        model.addAttribute("todos", todoService.findAll());
        return "todo-list";
    }

    @GetMapping("/todos/new")
    public String showCreateForm(Model model) {
        Todo todo = new Todo();
        todo.setStatus(TodoStatus.PENDING);
        todo.setPriority(TodoPriority.MEDIUM);
        model.addAttribute("todo", todo);
        return "todo-form";
    }

    @GetMapping("/todos/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return todoService.findById(id)
                .map(todo -> {
                    model.addAttribute("todo", todo);
                    return "todo-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("message", "Không tìm thấy công việc cần sửa.");
                    return "redirect:/";
                });
    }

    @PostMapping("/todos")
    public String saveTodo(@Valid @ModelAttribute("todo") Todo todo, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "todo-form";
        }
        boolean isUpdate = todo.getId() != null;
        todoService.save(todo);
        redirectAttributes.addFlashAttribute(
                "message",
                isUpdate ? "Cập nhập công việc thành công!" : "Thêm công việc thành công!"
        );
        return "redirect:/";
    }

    @GetMapping("/todos/delete/{id}")
    public String deleteTodo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = todoService.deleteById(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "Xoá công việc thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy công việc cần xoá.");
        }
        return "redirect:/";
    }
}
