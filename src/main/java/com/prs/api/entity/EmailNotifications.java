package com.prs.api.entity;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "email_notifications")
public class EmailNotifications {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notId")
	private Long notId;

	private String fromMail;
	private String toMail;
	private String subject;
	private String bodyContent;
	private String readFlag;
	private LocalDate sendDate;
	private Date createdDate;
	private Date updatedDate;

}
