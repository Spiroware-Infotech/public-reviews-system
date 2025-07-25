package com.prs.api.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "organization")
public class Organization {

	@Id
	private Long id;

	@NotEmpty
	private String firstname;
	private String lastname;

	@NotEmpty
	@Column(name = "companyname", unique = true,columnDefinition = "LONGTEXT")
	private String orgName;

	@Column(columnDefinition = "LONGTEXT")
	private String address;
	private String city;
	private String state;
	private String country;
	private String pincode;
	private double latitude;
	private double longitude;
	private String orgProfilePic;
	private Integer likes;
	private Integer dislikes;
	private String description;
	private Integer sharing;
	private Date createDate;
	private Date updatedDate;
	private String websitelink;
	private String youtubelink;
	private String instalink;
	private String xlink;
	private String fblink;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cat_id")
	private Category category;

	@OneToOne
	@MapsId
	@JoinColumn(name = "id")
	private User user;

	@OneToMany(mappedBy = "organization", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Reviews> reviews;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "plan_id")
	private Subscription subscription;
	
	@OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UploadFiles> organizationImages = new ArrayList<>();
	
	
}
