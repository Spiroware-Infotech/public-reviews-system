package com.prs.api.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prs.api.config.Utility;
import com.prs.api.dto.ChangePassword;
import com.prs.api.dto.LocationData;
import com.prs.api.dto.OrganizationDto;
import com.prs.api.dto.UsersDto;
import com.prs.api.entity.Category;
import com.prs.api.entity.Organization;
import com.prs.api.entity.PublicUser;
import com.prs.api.entity.Role;
import com.prs.api.entity.Subscription;
import com.prs.api.entity.User;
import com.prs.api.exceptions.EmailFoundException;
import com.prs.api.exceptions.UserFoundException;
import com.prs.api.exceptions.UserNotFountException;
import com.prs.api.mapper.OrganizationMapper;
import com.prs.api.mapper.PublicUserMapper;
import com.prs.api.org.repository.OrganizationRepository;
import com.prs.api.repository.CategoryRepository;
import com.prs.api.repository.PublicUserRepository;
import com.prs.api.repository.RoleRepository;
import com.prs.api.repository.SubscriptionRepository;
import com.prs.api.repository.UserRepository;
import com.prs.api.response.dto.OrganizationResponse;
import com.prs.api.response.dto.UserAllResponse;
import com.prs.api.response.dto.UserResponse;
import com.prs.api.service.LocationService;
import com.prs.api.service.UserService;
import com.prs.api.utils.DtoConverter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PublicUserRepository publicUserRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private LocationService locationService;
	@Autowired
	private OrganizationMapper organizationMapper;
	
	@Autowired
	private PublicUserMapper publicUserMapper;
	

	@Override
	public UserResponse findUserByUserName(String username) {
		User user = userRepository.findByUsername(username);
		UserResponse userResponse = DtoConverter.convertToDto(user, UserResponse.class);
		return userResponse;
	}

	// creating user
	@Override
	public User createUser(User user) throws Exception {
		User user1 = this.userRepository.findByUsername(user.getUsername());
		User user2 = this.userRepository.findByEmail(user.getEmail());

		if (user1 != null || user2 != null) {
			if (user2 != null) {
				throw new EmailFoundException();
			}
			throw new UserFoundException();
		} else {

			user1 = this.userRepository.save(user);
		}
		return user1;
	}

	// get user by user name
	@Override
	public User getUser(String username) {
		return this.userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String username) {
		return userRepository.findByEmail(username);
	}

	// delete user by user ID
	@Override
	public void deleteUser(Long userID) {
		this.userRepository.deleteById(userID);
	}

//    get all users
	@Override
	public List<User> getAllUser() {
		return new ArrayList<>(this.userRepository.findAll());
	}

	// update user
	@Override
	public User updateUser(User user, long userId) {
		User userUpdate = this.userRepository.findById(userId).get();
		userUpdate.setFirstname(user.getFirstname());
		userUpdate.setLastname(user.getLastname());
		// userUpdate.setEmail(user.getEmail());
		userUpdate.setPhoneNumber(user.getPhoneNumber());
		userUpdate.setVerificationCode(user.getVerificationCode());

		return this.userRepository.save(userUpdate);
	}

	// send verification code
	@Override
	public void sendVerificationEmail(User user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {

		String toAddress = user.getEmail();
		String fromAddress = "info@prs.com";
		String senderName = "Public Review System";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Exam Portal";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", user.getFirstname());
		String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);

		mailSender.send(message);
		log.info("Mail sent successfull!...");
	}

	// verify user by verification code
	@Override
	public boolean verify(String verificationCode) {
		User user = userRepository.findByVerificationCode(verificationCode);

		if (user == null || user.isEnabled()) {
			return false;
		} else {
			user.setVerificationCode(null);
			user.setActive(true);
			userRepository.save(user);
			return true;
		}
	}

	@Override
	public void updateResetPasswordToken(String token, String email) throws UserNotFountException {
		User user = userRepository.findByEmail(email);

		if (user != null) {
			user.setResetPasswordToken(token);
			userRepository.save(user);
		} else {
			throw new UserNotFountException("User not found with this email: " + email);
		}
	}

	@Override
	public User getResetPasswordToken(String resetToken) {
		return userRepository.findByResetPasswordToken(resetToken);
	}

	@Override
	public void updatePassword(User user, String newPassword) {
		user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
		user.setResetPasswordToken(null);

		userRepository.save(user);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public UserAllResponse saveUser(UsersDto usersDto, HttpServletRequest request, String roleName) throws Exception {
		log.info("UserServiceImpl --> saveUser -- Start");
		User info = new User();
		info.setFirstname(usersDto.getFirstname());
		info.setLastname(usersDto.getLastname());
		info.setPhoneNumber(usersDto.getPhoneNumber());
		info.setEmail(usersDto.getEmail());
		info.setUsername(usersDto.getUsername());
		String encodedPassword = bCryptPasswordEncoder.encode(usersDto.getPassword());
		info.setPassword(encodedPassword);
		info.setCreateTime(new Date());
		info.setActive(false);

		// Setting roleName by default
		Role role = roleRepository.findByName(roleName).isPresent() ? roleRepository.findByName(roleName).get() : null;
		Set<Role> userRoles = new HashSet<>();
		userRoles.add(role);
		info.setRoles(userRoles);

		User user = this.createUser(info);

		PublicUser publicUser = new PublicUser();
		publicUser.setUser(user);
		publicUser.setFirstname(info.getFirstname());
		publicUser.setLastname(info.getLastname());
		publicUser.setEmail(info.getEmail());
		publicUser.setPhone(info.getPhoneNumber());
		publicUser.setCreateddate(new Date());
		publicUser.setLastUpdateddate(new Date());
		publicUser.setCurrentStatus("INACTIVE");

		publicUser.setUser(user);

		publicUserRepository.save(publicUser);

		// generate random string as verifactioncode
		String verificationCode = RandomString.make(16);
		user.setVerificationCode(verificationCode);

		// send verification code
		String siteURL = Utility.getSiteUrl(request);
		sendVerificationEmailForOrg(info, siteURL);

		// update user details
		this.updateUser(user, user.getUserId());

		log.info("UserServiceImpl --> saveUser -- End");

		// convert entity object to response dto
		UserAllResponse userResponse = publicUserMapper.toDto(publicUser);
		userResponse.setRole(roleName);
		return userResponse;
	}

	@Override
	public OrganizationResponse saveOrganizationUser(@Valid OrganizationDto usersDto, String userType,
			HttpServletRequest request) throws UnsupportedEncodingException, Exception {
		log.info("UserServiceImpl --> saveOrganizationUser -- Start");
		User info = new User();
		info.setFirstname(usersDto.getFirstname());
		info.setLastname(usersDto.getLastname());
		info.setPhoneNumber(usersDto.getPhoneNumber());
		info.setEmail(usersDto.getEmail());
		info.setUsername(usersDto.getUsername());
		String encodedPassword = bCryptPasswordEncoder.encode(usersDto.getPassword());
		info.setPassword(encodedPassword);
		info.setCreateTime(new Date());
		info.setActive(false);

		// Setting roleName by default
		Role role = roleRepository.findByName("ROLE_ORGANIZATION").isPresent()
				? roleRepository.findByName("ROLE_ORGANIZATION").get()
				: null;
		Set<Role> userRoles = new HashSet<>();
		userRoles.add(role);
		info.setRoles(userRoles);

		User user = this.createUser(info);

		// generate random string as verifactioncode
		String verificationCode = RandomString.make(16);
		info.setVerificationCode(verificationCode);

		// send verification code
		String siteURL = Utility.getSiteUrl(request);
		sendVerificationEmailForOrg(info, siteURL);

		// update user details
		this.updateUser(user, user.getUserId());

		Category category = categoryRepository.findCategoryActiveById(usersDto.getCatId()).orElse(null);
		if (usersDto.getSubId() == null) {
			usersDto.setSubId(Long.valueOf(1));
		}
		Subscription subscription = subscriptionRepository.findSubscriptionActiveById(usersDto.getSubId()).orElse(null);

		Organization org = new Organization();
		org.setOrgName(usersDto.getOrgName());
		org.setFirstname(usersDto.getFirstname());
		org.setLastname(usersDto.getLastname());
		org.setAddress(usersDto.getAddress());
		org.setCity(usersDto.getCity());
		org.setState(usersDto.getState());
		org.setCountry(usersDto.getCountry());
		org.setCreateDate(new Date());
		org.setUpdatedDate(new Date());
		org.setPincode(usersDto.getPincode());

		org.setUser(user);
		org.setCategory(category);
		org.setSubscription(subscription);

		Mono<LocationData> location = locationService.getLocationByPincode(usersDto.getPincode());
		if (Objects.nonNull(location)) {
			LocationData data = location.block();
			org.setLatitude(Double.parseDouble(data.lat()));
			org.setLongitude(Double.parseDouble(data.lon()));
		}
		Organization organization = organizationRepository.save(org);

		// convert entity object to response dto
		OrganizationResponse orgResponse = organizationMapper.toDto(organization);
		orgResponse.setEmail(user.getEmail());
		orgResponse.setPhoneNumber(user.getPhoneNumber());
		orgResponse.setUsername(user.getUsername());

		log.info("UserServiceImpl --> saveOrganizationUser -- End");
		return orgResponse;

	}

	private void sendVerificationEmailForOrg(User user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = "info@prs.com";
		String senderName = "Public Review System";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>" + "Please use below code to verify your registration:<br>"
				+ "<h3>VERIFY CODE : [[code]]</h3>" + "Thank you,<br>" + "Exam Portal";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", user.getFirstname());
		// String verifyURL = siteURL + "/user/verify?code=" +
		// user.getVerificationCode();

		content = content.replace("[[code]]", user.getVerificationCode());

		helper.setText(content, true);

		mailSender.send(message);
		log.info("Mail sent successfull!...");
	}

	@Override
	public UserAllResponse getUserById(Long userId) throws UserNotFountException {
		PublicUser user = publicUserRepository.findById(userId)
				.orElseThrow(() -> new UserNotFountException("User not found with ID: " + userId));
		// convert entity object to response dto
		UserAllResponse userResponse = publicUserMapper.toDto(user);
		return userResponse;
	}

	@Override
	public boolean resendVerify(String email, HttpServletRequest request) throws Exception {
		User user = userRepository.findByEmail(email);

		if (user == null || user.isEnabled()) {
			return false;
		} else {
			String verificationCode = RandomString.make(16);
			user.setVerificationCode(verificationCode);

			// send verification code
			String siteURL = Utility.getSiteUrl(request);
			sendVerificationEmailForOrg(user, siteURL);

			// update user details
			this.updateUser(user, user.getUserId());

			return true;
		}
	}

	@Override
	public UserAllResponse updateUserProfile(@Valid UsersDto usersDto, HttpServletRequest request, String roleName)
			throws Exception {
		log.info("UserServiceImpl --> updateUser -- Start");
		User info = this.userRepository.findByUsername(usersDto.getUsername());
		if (info == null) {
			throw new UserFoundException();
		}
		info.setFirstname(usersDto.getFirstname());
		info.setLastname(usersDto.getLastname());
		info.setPhoneNumber(usersDto.getPhoneNumber());

		User user = this.userRepository.saveAndFlush(info);

		PublicUser publicUser = publicUserRepository.findById(user.getUserId()).orElse(null);
		if (publicUser == null) {
			throw new UserFoundException();
		}
		
		publicUser.setFirstname(info.getFirstname());
		publicUser.setLastname(info.getLastname());
		publicUser.setEmail(info.getEmail());
		publicUser.setPhone(info.getPhoneNumber());
		publicUser.setLastUpdateddate(new Date());
		publicUser.setAddress(usersDto.getAddress());
		publicUser.setCity(usersDto.getCity());
		publicUser.setState(usersDto.getState());
		publicUser.setCountry(usersDto.getCountry());
		publicUser.setZipcode(usersDto.getZipcode());
		publicUser.setGender(usersDto.getGender());

		publicUser.setUser(user);

		publicUserRepository.save(publicUser);

		// update user details
		this.updateUser(user, user.getUserId());

		log.info("UserServiceImpl --> updateUser -- End");

		// convert entity object to response dto
		UserAllResponse userResponse = publicUserMapper.toDto(publicUser);
		userResponse.setRole(roleName);
		return userResponse;
	}

	@Override
	public OrganizationResponse updateOrgProfile(@Valid OrganizationDto usersDto, HttpServletRequest request,
			String roleName) throws UserFoundException {
		log.info("UserServiceImpl --> updateOrgProfile -- Start");
		User info = this.userRepository.findByUsername(usersDto.getUsername());
		if (info == null) {
			throw new UserFoundException();
		}
		info.setFirstname(usersDto.getFirstname());
		info.setLastname(usersDto.getLastname());
		info.setPhoneNumber(usersDto.getPhoneNumber());

		User user = this.userRepository.saveAndFlush(info);

		Organization org = organizationRepository.findById(info.getUserId()).orElse(null);
		if (org == null) {
			throw new UserFoundException();
		}
		org.setFirstname(usersDto.getFirstname());
		org.setLastname(usersDto.getLastname());
		org.setAddress(usersDto.getAddress());
		org.setCity(usersDto.getCity());
		org.setState(usersDto.getState());
		org.setCountry(usersDto.getCountry());
		org.setUpdatedDate(new Date());
		org.setPincode(usersDto.getPincode());
		org.setFblink(usersDto.getFblink());
		org.setWebsitelink(usersDto.getWebsitelink());
		org.setXlink(usersDto.getXlink());
		org.setYoutubelink(usersDto.getYoutubelink());
		org.setDescription(usersDto.getDescription());
		
		org.setUser(user);

		Mono<LocationData> location = locationService.getLocationByPincode(usersDto.getPincode());
		if (Objects.nonNull(location)) {
			LocationData data = location.block();
			org.setLatitude(Double.parseDouble(data.lat()));
			org.setLongitude(Double.parseDouble(data.lon()));
		}
		Organization organization = organizationRepository.saveAndFlush(org);

		// convert entity object to response dto
		OrganizationResponse orgResponse = organizationMapper.toDto(organization);
		orgResponse.setEmail(user.getEmail());
		orgResponse.setPhoneNumber(user.getPhoneNumber());
		orgResponse.setUsername(user.getUsername());

		log.info("UserServiceImpl --> updateOrgProfile -- End");
		return orgResponse;

	}

	@Override
	public boolean checkExistingPassword(ChangePassword password, Long userId) {
		User info = userRepository.findById(userId).orElse(null);
		boolean matches = bCryptPasswordEncoder.matches(password.getOld_password(), info.getPassword());
		return matches;
	}

	@Override
	public User findUserById(Long userId) {
		User info = userRepository.findById(userId).orElse(null);
		return info;
	}

//	@Override
//	public User processOAuthPostLogin(OAuth2User oAuth2User) throws Exception {
//
//		String email = oAuth2User.getAttribute("email");
//
//		// Create new user
//		User newUser = new User();
//		newUser.setFirstname(oAuth2User.getAttribute("given_name"));
//		newUser.setLastname(oAuth2User.getAttribute("family_name"));
//		newUser.setEmail(email);
//		newUser.setUsername(oAuth2User.getAttribute("sub")); // or email.split("@")[0]
//		newUser.setPhoneNumber("00000000000");
//		newUser.setActive(true);
//		newUser.setCreateTime(new Date());
//		newUser.setPassword(bCryptPasswordEncoder.encode("oauth2user")); // Placeholder password
//
//		Role role = roleRepository.findByName("ROLE_USER").isPresent() ? roleRepository.findByName("ROLE_USER").get()
//				: null;
//		Set<Role> userRoles = new HashSet<>();
//		userRoles.add(role);
//		newUser.setRoles(userRoles);
//
//		User user = this.createUser(newUser);
//
//		return user;
//	}

}
