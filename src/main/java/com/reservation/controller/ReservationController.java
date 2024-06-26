package com.reservation.controller;

import com.reservation.dto.reservation.MakeReservation;
import com.reservation.dto.reservation.ReservationDto;
import com.reservation.entity.user.User;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.repository.ReservationRepository;
import com.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    //예약 요청
    @PostMapping("/request")
    public ResponseEntity<?> reservation(@RequestBody MakeReservation.Request request,
        @AuthenticationPrincipal TokenDto user) {
        request.setUserId(user.getEmail());
        request.setRestaurant(request.getRestaurant());
        ReservationDto reservationDto = reservationService.makeReservation(request);

        return ResponseEntity.ok(MakeReservation.Response.fromDto(reservationDto));
    }

    //예약 상세 정보 보기
    @GetMapping("/detail/{reservationId}")
    public ResponseEntity<?> reservationDetail(@PathVariable Long reservationId,
        @AuthenticationPrincipal TokenDto user) {
        ReservationDto reservationDto =
            reservationService.reservationDetail(reservationId, user);
        return ResponseEntity.ok(reservationDto);
    }

}