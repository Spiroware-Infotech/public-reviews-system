package com.prs.api.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.prs.api.dto.ChangePassword;
import com.prs.api.dto.OrganizationDto;
import com.prs.api.dto.UsersDto;
import com.prs.api.entity.User;
import com.prs.api.exceptions.UserFoundException;
import com.prs.api.exceptions.UserNotFountException;
import com.prs.api.response.dto.OrganizationResponse;
import com.prs.api.response.dto.UserAllResponse;
import com.prs.api.response.dto.UserResponse;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface UserService {

	// create user
	public User createUser(User user) throws Exception;

	// get user by user name
	public User getUser(String username);

	UserAllResponse getUserById(Long userId) throws UserNotFountException;

	// get all users
	public List<User> getAllUser();

	// delete user by id
	public void deleteUser(Long ID);

	// update user
	public User updateUser(User user, long userId);

	// send email verification code
	public void sendVerificationEmail(User user, String siteURL)
			throws MessagingException, UnsupportedEncodingException;

	// verify user
	public boolean verify(String verificationCode);

	// update reset password
	public void updateResetPasswordToken(String token, String email) throws UserNotFountException;

	public User getResetPasswordToken(String resetToken);

	public void updatePassword(User user, String newPassword);

	public User findByUsername(String username);

	public User findByEmail(String username);

	public UserAllResponse saveUser(UsersDto user, HttpServletRequest request, String roleName) throws Exception;

	public OrganizationResponse saveOrganizationUser(@Valid OrganizationDto userRequest, String userType,
			HttpServletRequest request) throws Exception;

	public UserResponse findUserByUserName(String name);

	public boolean resendVerify(String email, HttpServletRequest request) throws Exception;

	public UserAllResponse updateUserProfile(@Valid UsersDto userRequest, HttpServletRequest request, String roleName) throws Exception;

	public OrganizationResponse updateOrgProfile(@Valid OrganizationDto userRequest, HttpServletRequest request, String roleName) throws UserFoundException;

	public boolean checkExistingPassword(ChangePassword password, Long userId);

	public User findUserById(Long userId);

	// public User processOAuthPostLogin(OAuth2User oAuth2User) throws Exception;

}
