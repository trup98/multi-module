package com.ecommerce.utility.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonEnum {

    AUTHORIZATION("Authorization"),
    USER_ID("userId"),
    USER_ROLE("userRole"),
    USER_NAME("userName");
    private final String value;

}
