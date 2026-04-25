package com.holiday.ex01.controller;

import com.holiday.ex01.model.Todo;
import com.holiday.ex01.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        model.addAttribute("todo", new Todo());
        return "todo-form";
    }

    @PostMapping("/todos")
    public String createTodo(@Valid @ModelAttribute("todo") Todo todo,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "todo-form";
        }

        todoService.save(todo);
        return "redirect:/";
    }
}
