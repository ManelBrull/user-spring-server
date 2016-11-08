package com.mbrull.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mbrull.dto.ForgotPasswordForm;
import com.mbrull.dto.ResetPasswordForm;
import com.mbrull.dto.SignupForm;
import com.mbrull.services.UserService;
import com.mbrull.util.FlashAttributeMyUtils;
import com.mbrull.validator.ForgotPasswordFormValidator;
import com.mbrull.validator.ResetPasswordFormValidator;
import com.mbrull.validator.SignupFormValidator;

@Controller
public class RootController {

    private final UserService userService;
    private final SignupFormValidator signupFormValidator;
    private final ForgotPasswordFormValidator forgotPasswordFormValidator;
    private final ResetPasswordFormValidator resetPasswordFormValidator;

    private static final Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    public RootController(UserService userService, SignupFormValidator signupFormValidator,
            ForgotPasswordFormValidator forgotPasswordFormValidator,
            ResetPasswordFormValidator resetPasswordFormValidator) {
        this.userService = userService;
        this.signupFormValidator = signupFormValidator;
        this.forgotPasswordFormValidator = forgotPasswordFormValidator;
        this.resetPasswordFormValidator = resetPasswordFormValidator;

    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        model.addAttribute(new SignupForm());
        return "signup";
    }

    @InitBinder("signupForm")
    protected void initSignupBinder(WebDataBinder binder) {
        binder.setValidator(signupFormValidator);
    }

    @InitBinder("forgotPasswordForm")
    protected void initForgotPasswordBinder(WebDataBinder binder) {
        binder.setValidator(forgotPasswordFormValidator);
    }

    @InitBinder("resetPasswordForm")
    protected void initResetPasswordBinder(WebDataBinder binder) {
        binder.setValidator(resetPasswordFormValidator);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "signup";
        }

        userService.signup(signupForm);
        FlashAttributeMyUtils.flashSuccess(redirectAttributes, "signupSuccess");

        return "redirect:/";
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public String forgotPassword(Model model) {

        model.addAttribute(new ForgotPasswordForm());
        return "forgot-password";
    }

    /**
     * Forgot password
     */
    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public String forgotPassword(@ModelAttribute("forgotPasswordForm") @Valid ForgotPasswordForm forgotPasswordForm,
            BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors())
            return "forgot-password";

        userService.forgotPassword(forgotPasswordForm);
        FlashAttributeMyUtils.flashSuccess(redirectAttributes, "checkMailResetPassword");

        return "redirect:/";
    }

    /**
     * Reset password
     */
    @RequestMapping(value = "/reset-password/{forgotPasswordCode}")
    public String resetPassword(@PathVariable("forgotPasswordCode") String forgotPasswordCode, Model model) {

        model.addAttribute(new ResetPasswordForm());
        return "reset-password";
    }

    @RequestMapping(value = "/reset-password/{forgotPasswordCode}", method = RequestMethod.POST)
    public String resetPassword(@PathVariable("forgotPasswordCode") String forgotPasswordCode,
            @ModelAttribute("resetPasswordForm") @Valid ResetPasswordForm resetPasswordForm, BindingResult result,
            RedirectAttributes redirectAttributes) {

        userService.resetPassword(forgotPasswordCode, resetPasswordForm, result);

        if (result.hasErrors()) {
            return "reset-password";
        }
        FlashAttributeMyUtils.flashSuccess(redirectAttributes, "passwordChanged");
        return "redirect:/login";
    }

}
