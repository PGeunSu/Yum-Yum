package com.reservation.exception;

import com.reservation.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public ReservationException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
