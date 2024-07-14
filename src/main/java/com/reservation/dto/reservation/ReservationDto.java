package com.reservation.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reservation.dto.reservation.util.TimeParsingUtils;
import com.reservation.entity.reservation.Reservation;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ReservationDto {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private String startTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private String endTime;

  private Long userId;
  private String userEmail;

  private Long restaurantId;
  private String post; //상호명

//        public static ReservationDto fromEntity(Reservation reservation){
//            return ReservationDto.builder()
//                .id(reservation.getId())
//                .time(String.valueOf(reservation.getTime()))
//                .place(reservation.getPlace())
//                .name(reservation.getName())
//                .status(reservation.getReservationStatus())
//                .createdAt(reservation.getCreatedAt())
//                .build();
//        }

  public static ReservationDto toDto(Reservation reservation) {
    return ReservationDto.builder()
        .startTime(TimeParsingUtils.formatterString(reservation.getStartTime()))
        .endTime(TimeParsingUtils.formatterString(reservation.getEndTime()))
        .userId(reservation.getUser().getId())
        .userEmail(reservation.getUser().getEmail())
        .restaurantId(reservation.getRestaurant().getId())
        .post(reservation.getRestaurant().getPost())
        .build();
  }
}
