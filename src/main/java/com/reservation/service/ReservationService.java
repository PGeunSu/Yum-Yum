package com.reservation.service;


import static com.reservation.exception.ErrorCode.DUPLICATED_RESERVATION;
import static com.reservation.exception.ErrorCode.RESERVATION_NOT_FOUND;
import static com.reservation.exception.ErrorCode.RESERVATION_TIME_WRONG;
import static com.reservation.exception.ErrorCode.STORE_NOT_FOUND;
import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;

import com.reservation.dto.reservation.ReservationCommand;
import com.reservation.dto.reservation.ReservationDto;
import com.reservation.dto.reservation.ValidateFindByIdDto;
import com.reservation.dto.reservation.util.TimeParsingUtils;
import com.reservation.entity.reservation.Reservation;
import com.reservation.entity.reservation.Restaurant;
import com.reservation.entity.user.User;
import com.reservation.exception.Exception;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.repository.ReservationRepository;
import com.reservation.repository.RestaurantRepository;
import com.reservation.repository.UserRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final UserRepository userRepository;
  private final RestaurantRepository restaurantRepository;

  @Transactional
  public ReservationDto register(Long restaurantId,
      ReservationCommand.RegisterReservation registerReservation){
    Reservation reservation = validateCreateRequest(restaurantId, registerReservation);
    Reservation saveReservation = reservationRepository.save(reservation);
    ReservationDto reservationDto = ReservationDto.toDto(saveReservation);

    return reservationDto;
  }

  private Reservation validateCreateRequest(Long restaurantId,
      ReservationCommand.RegisterReservation registerReservation){

    //시간 간격 최소 1시간
    validateDiffTime(registerReservation.getStartTime(), registerReservation.getEndTime());

    ValidateFindByIdDto validateFindByIdDto = validateFindById(registerReservation.getUserId(), restaurantId);

    LocalDateTime startTime = TimeParsingUtils.formatterLocalDateTime(registerReservation.getStartTime());
    LocalDateTime endTime = TimeParsingUtils.formatterLocalDateTime(registerReservation.getEndTime());

    //예약 중복 체크
    List<Reservation> findReservation = reservationRepository.findByRestaurantIdAndTime(restaurantId, startTime, endTime);
    if (findReservation != null){
      throw new Exception(DUPLICATED_RESERVATION);
    }

    Reservation reservation = registerReservation.toEntity(
        validateFindByIdDto.getUser(),
        validateFindByIdDto.getRestaurant()
    );

    return reservation;
  }

  // 해당  예약 조회 (시간 이용)
  public List<ReservationDto> getTimePeriod(Long restaurantId, String startTime, String endTime){
    validateDiffTime(startTime, endTime);

    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new Exception(STORE_NOT_FOUND));

    LocalDateTime eTime = TimeParsingUtils.formatterLocalDateTime(endTime);
    LocalDateTime sTime = TimeParsingUtils.formatterLocalDateTime(startTime);

    return reservationRepository.findByRestaurantIdAndTimePeriod(restaurant.getId(), sTime, eTime)
        .stream().map(ReservationDto::toDto)
        .collect(Collectors.toList());
  }

  //해당 예약 조회 (시간 사용 x)
  public List<ReservationDto> getByRestaurantId(Long restaurantId){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new Exception(STORE_NOT_FOUND));

    return reservationRepository.findByRestaurantId(restaurantId)
        .stream().map(ReservationDto::toDto)
        .collect(Collectors.toList());
  }

  // 해당 유저 예약 조회
  public List<ReservationDto> getAllByUserId(Long userId){
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new Exception(USER_NOT_FOUND));

    return reservationRepository.findByUserId(user.getId())
        .stream().map(ReservationDto::toDto)
        .collect(Collectors.toList());
  }

  //예약 수정
  @Transactional
  public void updateReservation(Long reservationId,
      ReservationCommand.UpdateReservation updateReservation){
    Reservation findReservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new Exception(RESERVATION_NOT_FOUND));

    validateUpdate(updateReservation);
    findReservation.update(updateReservation);
  }

  //예약 삭제
  @Transactional
  public void deleteReservation(Long reservationId){
    Reservation findReservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new Exception(RESERVATION_NOT_FOUND));

    reservationRepository.deleteById(reservationId);
  }



  private void validateDiffTime(String starTime, String endTime){
    SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    Date startDate = null;
    Date endDate = null;
    try{
      startDate = sft.parse(starTime);
      endDate = sft.parse(endTime);
    }catch (ParseException e){
      throw new RuntimeException(e);
    }
    long diffTime = (endDate.getTime() - startDate.getTime()) / 3600000;

    if (diffTime < 1 || diffTime > 4){
      throw new Exception(RESERVATION_TIME_WRONG);
    }
  }

  private void validateUpdate(ReservationCommand.UpdateReservation updateReservation){
    validateDiffTime(updateReservation.getStartTime(), updateReservation.getEndTime());
    validateFindById(updateReservation.getUserId(), updateReservation.getRestaurantId());

  }

  private ValidateFindByIdDto validateFindById(Long userId, Long restaurantId){
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new Exception(USER_NOT_FOUND));
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new Exception(STORE_NOT_FOUND));

    return ValidateFindByIdDto.builder()
        .user(user)
        .restaurant(restaurant)
        .build();
  }

  //jwt 의 회원정보가 reservation 의 userId 일치 확인
  private boolean validateReservationAccessAuthority(TokenDto user, Reservation reservation) {
    if (reservation.getUser().getId().equals(user.getId())) {
      log.info("UserID : {}, 예약 내역 확인", user.getId());
      return true;
    }
    return false;
  }


}
