package com.reservation.service;


import static com.reservation.exception.ErrorCode.ACCESS_DENIED;
import static com.reservation.exception.ErrorCode.RESERVATION_NOT_FOUND;
import static com.reservation.exception.ErrorCode.STORE_NOT_FOUND;
import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;

import com.reservation.dto.reservation.MakeReservation;
import com.reservation.dto.reservation.MakeReservation.Request;
import com.reservation.dto.reservation.ReservationDto;
import com.reservation.entity.reservation.Reservation;
import com.reservation.entity.reservation.Restaurant;
import com.reservation.entity.user.User;
import com.reservation.exception.Exception;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.repository.ReservationRepository;
import com.reservation.repository.RestaurantRepository;
import com.reservation.repository.UserRepository;
import com.reservation.type.ReservationStatus;
import java.time.LocalDateTime;
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


        //user- 매장 예약
         public ReservationDto makeReservation(MakeReservation.Request request){
            Reservation reservation = makeReservationEntity(request);
            Reservation saved = reservationRepository.save(reservation);
            log.info("reservation id : {}", saved.getId());
            return ReservationDto.fromEntity(reservation);
        }

        //매장 예약 Request를 바탕으로 Reservation(entity) 생성
        private Reservation makeReservationEntity(MakeReservation.Request request){
            User user = userRepository.findByEmail(request.getUserId())//id 관련부분 구현필요.
                .orElseThrow(() -> new Exception(USER_NOT_FOUND));
            // 매장 예약이 진행되는 부분
            Restaurant restaurant = restaurantRepository.findByPost(request.getRestaurant())
                .orElseThrow(() -> new Exception(STORE_NOT_FOUND));
//            LocalDateTime reservationTime = LocalDateTime.of(request.getDate(), request.getTime());

            return Reservation.builder()
                .user(user)
                .restaurant(restaurant)
                .name(user.getName())
                .place(restaurant.getPost())
                .reservationStatus(ReservationStatus.REQUESTING)
                .createdAt(LocalDateTime.now())
                .build();
        }

        //user - 예약 상세 정보
        public ReservationDto reservationDetail(Long id, TokenDto user){
            Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new Exception(RESERVATION_NOT_FOUND));

            if (!this.validateReservationAccessAuthority(user, reservation)) {
                throw new Exception(ACCESS_DENIED);
            }

            return ReservationDto.fromEntity(reservation);
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
