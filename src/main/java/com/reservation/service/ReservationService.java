package com.reservation.service;


import com.reservation.dto.reservation.MakeReservation;
import com.reservation.dto.reservation.MakeReservation.Request;
import com.reservation.dto.reservation.ReservationDto;
import com.reservation.entity.reservation.Reservation;
import com.reservation.entity.reservation.Restaurant;
import com.reservation.entity.user.User;
import com.reservation.exception.ErrorCode;
import com.reservation.exception.ReservationException;
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
        private Reservation makeReservationEntity(Request request){
            User user = userRepository.findByEmail(request.getUserId())//id 관련부분 구현필요.
                .orElseThrow(() -> new ReservationException(ErrorCode.USER_NOT_FOUND));
            // 매장 예약이 진행되는 부분
            Restaurant restaurant = restaurantRepository.findByPost(request.getRestaurant())
                .orElseThrow(() -> new ReservationException(ErrorCode.STORE_NOT_FOUND));
//            LocalDateTime reservationTime = LocalDateTime.of(request.getDate(), request.getTime());

            return Reservation.builder()
                .name(user.getName())
                .id(user.getId())
                .place(restaurant.getPost())
                .reservationStatus(ReservationStatus.REQUESTING)
                .createdAt(LocalDateTime.now())
//                .time(reservationTime)
                .build();
        }

        //user - 예약 상세 정보
        public ReservationDto reservationDetail(Long id, String username){
            Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationException(ErrorCode.RESERVATION_NOT_FOUND));

            if (!this.validateReservationAccessAuthority(username, reservation)) {
                throw new ReservationException(ErrorCode.ACCESS_DENIED);
            }

            return ReservationDto.fromEntity(reservation);
        }


        //userDetails의 username이 reservationDto의 userId 일치 확인
        private boolean validateReservationAccessAuthority(String username, Reservation reservation) {
            if (reservation.getUser().equals(username)) {
                log.info("UserID : {}, 예약 내역 확인", username);
                return true;
            }
            return false;
        }


}
