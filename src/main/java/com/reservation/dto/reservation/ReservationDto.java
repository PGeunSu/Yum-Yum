package com.reservation.dto.reservation;

import com.reservation.entity.reservation.Reservation;
import com.reservation.type.ReservationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

    @Data
    @Builder
    public class ReservationDto {
        private Long id;

        private String time;
        private String place;
        private String name;

        @Enumerated(EnumType.STRING)
        private ReservationStatus status;
        private LocalDateTime createdAt;

        public static ReservationDto fromEntity(Reservation reservation){
            return ReservationDto.builder()
                .id(reservation.getId())
                .time(String.valueOf(reservation.getTime()))
                .place(reservation.getPlace())
                .name(reservation.getName())
                .status(reservation.getReservationStatus())
                .createdAt(reservation.getCreatedAt())
                .build();
        }
}
