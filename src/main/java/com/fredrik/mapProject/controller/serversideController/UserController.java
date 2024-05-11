package com.fredrik.mapProject.controller.serversideController;

import com.fredrik.mapProject.config.AppPasswordConfig;
import com.fredrik.mapProject.config.Roles;
import com.fredrik.mapProject.model.databaseEntity.UserEntity;
import com.fredrik.mapProject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Controller
public class UserController {

    private final UserService userService;
    private final AppPasswordConfig appPasswordConfig; // Bcrypt

    @Autowired
    public UserController(UserService userService, AppPasswordConfig appPasswordConfig) {
        this.appPasswordConfig = appPasswordConfig;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerUserPage(UserEntity userEntity, Model model) {
        model.addAttribute("roles", Roles.values());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            UserEntity userEntity,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            return "register";
        }

        userEntity.setPassword(
                appPasswordConfig.bCryptPasswordEncoder().encode(userEntity.getPassword())
        );

        userEntity.setRole(Roles.USER);

        userService.createNewUser(userEntity);

        return "redirect:/login";
    }

    @GetMapping("admin-page")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAllUsers(UserEntity userEntity, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<UserEntity> users = userService.getAll();

        model.addAttribute("users", users);
        model.addAttribute("roles", Roles.values());

        return "admin-page";
    }

    @PostMapping("/edit-user")
    @PreAuthorize("hasRole('ADMIN')")
    public String editUser(
            @RequestParam("userId") UUID userId,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("role") Roles role)
    {

        UserEntity user = userService.findById(userId).orElse(null);
        if (user == null) {
            return "error-page";
        }

        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        userService.updateUser(user);

        return "redirect:/admin-page";
    }

    @GetMapping("/delete-user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable("id") UUID userId) {

        Optional<UserEntity> user = userService.findById(userId);
        if (user.isEmpty()) {
            return "error-page";
        }

        userService.deleteUser(user.get());

        return "redirect:/admin-page";
    }

}
