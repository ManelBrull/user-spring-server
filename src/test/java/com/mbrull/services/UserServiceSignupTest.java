package com.mbrull.services;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.mbrull.Application;
import com.mbrull.dto.SignupForm;
import com.mbrull.entities.User;
import com.mbrull.mail.MailSender;
import com.mbrull.repositories.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
public class UserServiceSignupTest {
    
    UserService userService;
    
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    MailSender mailSender;
    
    @Before
    public void setUpUserService() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        mailSender = Mockito.mock(MailSender.class);
        
        userService = new UserServiceImpl(
                userRepository, 
                passwordEncoder, 
                mailSender);
    }
    
    
    @Test
    public void signupFormTestHappyPath() throws MessagingException{
        SignupForm happyPath = new SignupForm();
        String validEmail = "validEmail@gmail.com"; 
        happyPath.setEmail(validEmail);
        happyPath.setName("ValidName");
        happyPath.setPassword("abcd1234");
        happyPath.setUsername("ValidUsername");
        
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(new User());
        userService.signup(happyPath);
        TransactionSynchronizationManager.getSynchronizations().get(0).afterCommit();
        
        Mockito.verify(userRepository).save(Mockito.any(User.class));
        Mockito.verify(mailSender).send(Mockito.eq(validEmail), Mockito.anyString(), Mockito.anyString());
        
    }

}
