package com.mbrull.services;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.BindingResult;

import com.mbrull.dto.ChangePasswordForm;
import com.mbrull.dto.ForgotPasswordForm;
import com.mbrull.dto.ResetPasswordForm;
import com.mbrull.dto.SignupForm;
import com.mbrull.dto.UserDetailsImpl;
import com.mbrull.dto.UserEditForm;
import com.mbrull.entities.User;
import com.mbrull.entities.User.Role;
import com.mbrull.mail.MailSender;
import com.mbrull.repositories.UserRepository;
import com.mbrull.util.MyUtil;
import com.mbrull.util.PropertiesMyUtils;
import com.mbrull.util.UserUtil;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void signup(SignupForm signupForm) {

        final User user = new User();
        user.setName(signupForm.getName());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setEmail(signupForm.getEmail());
        user.getRoles().add(Role.UNVERIFIED);
        user.setVerificationCode(RandomStringUtils.randomAlphanumeric(16));
        userRepository.save(user);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                String verificationLink = PropertiesMyUtils.hostUri() + "/users/" + user.getVerificationCode()
                        + "/verify";
                try {
                    mailSender.send(
                            user.getEmail(), 
                            MyUtil.getMessage("verifySubject"),
                            MyUtil.getMessage("verifyEmail", verificationLink));
                            
                    logger.info("Verification emails sent to: " + user.getEmail() + " queued.");
                } catch (MessagingException e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            }
        });
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userId);
        if (user == null) {
            throw new UsernameNotFoundException(userId);
        }
        return new UserDetailsImpl(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void verify(String verificationCode) {

        long loggedInUserId = UserUtil.getSessionUser().getId();
        User user = userRepository.findOne(loggedInUserId);
        MyUtil.validate(user.getRoles().contains(Role.UNVERIFIED), "alreadyVerified");
        MyUtil.validate(user.getVerificationCode().equals(verificationCode), "incorrect", "verification code");

        user.getRoles().remove(Role.UNVERIFIED);
        user.setVerificationCode(null);
        userRepository.save(user);
    }

    @Override
    public void reSendVerificationEmail() {
        long loggedInUserId = UserUtil.getSessionUser().getId();
        User user = userRepository.findOne(loggedInUserId);
        user.getRoles().add(Role.UNVERIFIED);
        user.setVerificationCode(RandomStringUtils.randomAlphanumeric(16));
        userRepository.save(user);

        final User userToSendEmail = user;

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                String verificationLink = PropertiesMyUtils.hostUri() + "/users/"
                        + userToSendEmail.getVerificationCode() + "/verify";
                try {
                    mailSender.send(
                            userToSendEmail.getEmail(), 
                            MyUtil.getMessage("verifySubject"),
                            MyUtil.getMessage("verifyEmail", verificationLink));
                    logger.info("Verification emails sent to: " + userToSendEmail.getEmail() + " queued.");
                } catch (MessagingException e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void forgotPassword(ForgotPasswordForm form) {

        final User user = userRepository.findByEmail(form.getEmail());
        final String forgotPasswordCode = RandomStringUtils.randomAlphanumeric(User.RANDOM_CODE_LENGTH);

        user.setForgotPasswordCode(forgotPasswordCode);
        final User savedUser = userRepository.save(user);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                try {
                    mailForgotPasswordLink(savedUser);
                } catch (MessagingException e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            }

        });

    }

    protected void mailForgotPasswordLink(User savedUser) throws MessagingException {
        String forgotPasswordLink = PropertiesMyUtils.hostUri() + "/reset-password/"
                + savedUser.getForgotPasswordCode();
        mailSender.send(savedUser.getEmail(), MyUtil.getMessage("forgotPasswordSubject"), forgotPasswordLink);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void resetPassword(String forgotPasswordCode, ResetPasswordForm resetPasswordForm, BindingResult result) {

        User user = userRepository.findByForgotPasswordCode(forgotPasswordCode);
        if (user == null)
            result.reject("invalidForgotPassword");

        if (result.hasErrors())
            return;

        user.setForgotPasswordCode(null);
        user.setPassword(passwordEncoder.encode(resetPasswordForm.getPassword().trim()));
        userRepository.save(user);
    }

    @Override
    public User findOne(long userId) {
        User loggedIn = UserUtil.getSessionUser();
        User user = userRepository.findOne(userId);
        if (loggedIn == null || loggedIn.getId() != user.getId() && !loggedIn.isAdmin()) {
            user.setEmail("confindential");
        }
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(long userId, UserEditForm userEditForm) {

        User loggedIn = UserUtil.getSessionUser();
        MyUtil.validate(loggedIn.isAdmin() || loggedIn.getId() == userId, "noPermissions");
        User user = userRepository.findOne(userId);
        user.setName(userEditForm.getName());
        if (loggedIn.isAdmin())
            user.setRoles(userEditForm.getRoles());
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordForm changePasswordForm, BindingResult result) {
        logger.info("User service change password");
    }
    
    

}
