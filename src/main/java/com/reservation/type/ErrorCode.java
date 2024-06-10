package com.reservation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ALREADY_EXIST_STORE("이미 존재하는 매장 이름입니다."),
    STORE_NOT_FOUND("존재하지 않는 매장입니다.");
    private final String description;
}

