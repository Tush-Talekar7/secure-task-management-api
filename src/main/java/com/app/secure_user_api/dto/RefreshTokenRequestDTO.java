package com.app.secure_user_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenRequestDTO {

    String accessToken;
    String refreshToken;

}
