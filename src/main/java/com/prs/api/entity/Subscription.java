package com.prs.api.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "subscription")
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subId")
	private Long subId;

	private String planName;
	private String description;
	private Double price;
	private LocalDate subscriptionStart;
	private LocalDate subscriptionEnd;
	private Date createdDate;
	private Date updatedDate;
	
	@Column(name = "enabled",columnDefinition = "TINYINT")
	private Boolean enabled;

	@OneToMany(mappedBy = "subscription")
	private List<Organization> organizations;
}
