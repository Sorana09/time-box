package com.example.time.box.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateInvitationRequest {
    private String invitedEmail;
    private String description;
    private String invitedBy;
}
