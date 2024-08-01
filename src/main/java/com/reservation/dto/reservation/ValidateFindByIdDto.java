package com.reservation.dto.reservation;

import com.reservation.entity.reservation.Reservation;
import com.reservation.entity.reservation.Restaurant;
import com.reservation.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateFindByIdDto {

  private User user;
  private Restaurant restaurant;
  private Reservation reservation;

}
