package com.reservation.dto.reservation;

import com.reservation.dto.reservation.util.TimeParsingUtils;
import com.reservation.entity.reservation.Reservation;
import com.reservation.entity.reservation.Restaurant;
import com.reservation.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class ReservationCommand {

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  public static class RegisterReservation {

    private Long userId;
    private String startTime;
    private String endTime;

    public Reservation toEntity(User user, Restaurant restaurant) {
      return Reservation.builder()
          .startTime(TimeParsingUtils.formatterLocalDateTime(startTime))
          .endTime(TimeParsingUtils.formatterLocalDateTime(endTime))
          .user(user)
          .restaurant(restaurant)
          .place(restaurant.getPost())
          .name(user.getName())
          .build();
    }
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  public static class UpdateReservation {

    private Long userId;
    private Long restaurantId;
    private String startTime;
    private String endTime;

    public Reservation toEntity(ValidateFindByIdDto validateFindByIdDto) {
      return Reservation.builder()
          .startTime(TimeParsingUtils.formatterLocalDateTime(startTime))
          .endTime(TimeParsingUtils.formatterLocalDateTime(endTime))
          .user(validateFindByIdDto.getUser())
          .restaurant(validateFindByIdDto.getRestaurant())
          .build();
    }
  }


}
