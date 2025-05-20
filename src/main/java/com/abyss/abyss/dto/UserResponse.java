package com.abyss.abyss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private long id;
    private String username;
    private String role;

}
