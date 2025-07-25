package com.prs.api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.prs.api.enums.Sentiments;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "reviews")
public class Reviews {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "revId")
	private Long revId;

	private String subject; // e.g., "Great Staff Behavior", "Poor Cleanliness"
	private String comment; // full review text
	private Double rating; // 1 to 5 stars
	private LocalDateTime submittedAt = LocalDateTime.now();
	private String badFlag;
	private Integer likes;
	private Integer dislikes;
	private String images[];

	@Enumerated(EnumType.STRING)
	private Sentiments sentiment;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private PublicUser publicUser;

	@ManyToOne
	@JoinColumn(name = "organization_id", nullable = false)
	private Organization organization;
	
	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<UploadFiles> reviewImages = new ArrayList<>();
	
	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TagLines> tagLines = new ArrayList<>();
	
}
