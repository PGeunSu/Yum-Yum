package com.reservation.dto.reservation;


import com.reservation.type.ReservationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
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

//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//        private LocalDate date;
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
//        private LocalTime time;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private String user;
        private String restaurant;

        private String time;
        private String place;
        private String name;

        @Enumerated(EnumType.STRING)
        private ReservationStatus status;
        private LocalDateTime createdAt;



        public static Response fromDto(ReservationDto reservationDto) {
            return Response.builder()
                .user(reservationDto.getUser())
                .restaurant(reservationDto.getRestaurant())
                .time(reservationDto.getTime())
                .place(reservationDto.getPlace())
                .status(reservationDto.getStatus())
                .createdAt(LocalDateTime.now())//getCreatedAt()
                .build();
        }

    }
}
