package com.reservation.dto.reservation;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.reservation.type.ReservationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class MakeReservation {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private String userId;
        private String restaurant;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private String time;
        private String place;
        private String name;

        @Enumerated(EnumType.STRING)
        private ReservationStatus status;
        private LocalDateTime createdAt;

        public static Response fromDto(ReservationDto reservationDto) {
            return Response.builder()
                .time(reservationDto.getTime())
                .place(reservationDto.getPlace())
                .status(reservationDto.getStatus())
                .createdAt(LocalDateTime.now())//getCreatedAt()
                .build();
        }

    }
}
