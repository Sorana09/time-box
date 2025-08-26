package com.example.time.box.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "invitations")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InvitationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invitation_seq")
    @SequenceGenerator(name = "invitation_seq", sequenceName = "invitation_sequence", allocationSize = 1)
    private Long id;

    @Column
    private String invitationToken;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String invitedEmail;

    @Column(nullable = false)
    private String invitedBy;

    @Column
    private OffsetDateTime createdAt;

    @Column
    private Boolean accepted;
}
