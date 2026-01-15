package com.denso.anomaly_training_backend.dto.request;

import com.denso.anomaly_training_backend.enums.Role;
import lombok.Data;


@Data
public class UserRequest {

    private String username;
    private String password;
    private String fullName;
    private String email;
    private Role role;
    private Boolean isActive;

}