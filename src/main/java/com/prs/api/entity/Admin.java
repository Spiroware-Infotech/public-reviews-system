package com.prs.api.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Admin {

	@Id
	@GeneratedValue
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	private String firstname;
	private String lastname;
	private String email;
	private String gender;
	private String city;
	private String state;
	private String country;
	private String zipcode;
	private String address;
	private String phone;
	private String currentStatus;
	private Date lastUpdateddate;
	private Date createddate;
	private Date dob;
	private String bloodgroup;
	private String remarks;
	
//	@Lob
//	private byte[] profileImg;
	
	private String religion;

	// Relationship to this admin's user account
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relationship to parent user (creator)
    @ManyToOne
    @JoinColumn(name = "parent_user_id", nullable = false)
    private User parentUser;
}
