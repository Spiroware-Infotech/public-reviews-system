package com.prs.api.entity;

import com.prs.api.enums.UploadType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@Table(name = "uploadfiles")
public class UploadFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private String fileUrl;     // or use byte[] if storing in DB

    @Lob
    private byte[] data;        // optional, only if storing file content in DB

    @Enumerated(EnumType.STRING)
    private UploadType uploadType;  // USER, REVIEW, ORG, etc.
    private Long referenceId;       // ID of the related User/Org/Review
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Reviews review;  // Associated review if uploadType is REVIEW

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;  // Associated organization if uploadType is ORGANIZATION
}