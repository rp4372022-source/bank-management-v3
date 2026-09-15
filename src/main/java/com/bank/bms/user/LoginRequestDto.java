package com.bank.bms.user;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
