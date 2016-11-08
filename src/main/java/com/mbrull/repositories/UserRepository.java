package com.mbrull.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbrull.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	User findByForgotPasswordCode(String forgotPasswordCode);

}
