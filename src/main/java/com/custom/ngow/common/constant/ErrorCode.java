package com.custom.ngow.common.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {

    E400100(400, "Bad Request"),
    E400101(400, "Invalid Data Format: "),
    E400102(400, "User is existed"),
    E401100(401, "Unauthenticated"),
    E403100(403, "Forbidden"),
    E404100(404, "Not Found"),
    E404101(404, "User not existed"),

    E500100(500, "Internal Server Error");
    private final int value;
    private final String message;

    ErrorCode(int value, String message) {
        this.value = value;
        this.message = message;
    }
}
