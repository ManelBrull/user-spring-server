package com.mbrull.services;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.mbrull.Application;
import com.mbrull.mail.MailSender;
import com.mbrull.repositories.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
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
    public void nullSignupForm(){
        userService.signup(null);
    }

}
