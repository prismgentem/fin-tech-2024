package org.example.crudkudago.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    private String username;
    private String newPassword;
    private String confirmPassword;
    private String verificationCode;
}
