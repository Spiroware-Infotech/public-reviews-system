package com.prs.api.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PublicUser {

	@Id
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
	@Lob
	private byte[] profileImg;
	private String religion;

	@OneToOne
	@MapsId
	@JoinColumn(name = "id")
	private User user;

	@OneToMany(mappedBy = "publicUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Reviews> reviews;
}
