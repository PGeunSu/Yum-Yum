package com.reservation.controller;

import com.reservation.dto.reservation.MakeReservation;
import com.reservation.dto.reservation.ReservationDto;
import com.reservation.entity.user.User;
import com.reservation.service.ReservationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReservationUserController {

    private final ReservationService reservationService;

    //예약 요청
    @ApiOperation(value = "예약 요청")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/reservation/request")
    public ResponseEntity<?> reservation(@RequestBody MakeReservation.Request request,
        @AuthenticationPrincipal User user) {
        request.setUserId(String.valueOf(user.getId()));
        ReservationDto reservationDto = reservationService.makeReservation(request);

        return ResponseEntity.ok(MakeReservation.Response.fromDto(reservationDto));
    }

}