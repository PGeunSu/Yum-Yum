package com.reservation.controller;

import com.reservation.dto.reservation.ReservationDto;
import com.reservation.service.ReservationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약 상세 정보 보기
     */
    @ApiOperation(value = "예약 상세 정보", notes = "예약 ID")
    @GetMapping("/reservation/detail/{reservationId}")
    public ResponseEntity<?> reservationDetail(@PathVariable Long reservationId,
        @AuthenticationPrincipal UserDetails userDetails) {
        ReservationDto reservationDto =
            reservationService.reservationDetail(reservationId, userDetails.getUsername());
        return ResponseEntity.ok(reservationDto);
    }

}