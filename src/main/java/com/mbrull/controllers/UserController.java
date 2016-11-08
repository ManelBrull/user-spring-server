package com.mbrull.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mbrull.dto.UserEditForm;
import com.mbrull.entities.User;
import com.mbrull.services.UserService;
import com.mbrull.util.FlashAttributeMyUtils;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/{verificationCode}/verify")
    public String verify(@PathVariable("verificationCode") String verificationCode,
            RedirectAttributes redirectAttributes, HttpServletRequest request) throws ServletException {

        userService.verify(verificationCode);
        FlashAttributeMyUtils.flashSuccess(redirectAttributes, "verificationSuccess");
        request.logout();

        return "redirect:/";
    }

    @RequestMapping("/resend-verification-mail")
    public String resendVerificationMail(RedirectAttributes redirectAttributes) {
        userService.reSendVerificationEmail();

        FlashAttributeMyUtils.flashSuccess(redirectAttributes, "signupSuccess");

        return "redirect:/";

    }

    @RequestMapping("/{userId}")
    public String getById(@PathVariable("userId") long userId, Model model) {
        model.addAttribute("user", userService.findOne(userId));
        return "user";
    }

    @RequestMapping(value = "/{userId}/edit")
    public String edit(@PathVariable("userId") long userId, Model model) {

        User user = userService.findOne(userId);
        UserEditForm form = new UserEditForm();
        form.setName(user.getName());
        form.setRoles(user.getRoles());
        model.addAttribute(form);
        return "user-edit";
    }

    @RequestMapping(value = "/{userId}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable("userId") long userId,
            @ModelAttribute("userEditForm") @Valid UserEditForm userEditForm, BindingResult result,
            RedirectAttributes redirectAttributes, HttpServletRequest request) throws ServletException {

        if (result.hasErrors()) {
            return "user-edit";
        }

        userService.update(userId, userEditForm);
        FlashAttributeMyUtils.flashSuccess(redirectAttributes, "editSuccessful");
        request.logout();

        return "redirect:/";
    }
}
