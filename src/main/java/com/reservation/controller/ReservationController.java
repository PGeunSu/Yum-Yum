package com.reservation.controller;

import com.reservation.dto.reservation.ReservationCommand;
import com.reservation.dto.reservation.ReservationDto;
import com.reservation.entity.user.User;
import com.reservation.service.ReservationService;
import com.reservation.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    //예약 등록
    @PostMapping("/register/{restaurantId}")
    public ResponseEntity<ReservationDto> reservation(
        @RequestBody ReservationCommand.RegisterReservation registerReservation,
        @PathVariable("restaurantId") Long restaurantId, Authentication auth
    ){
        User user = userService.getUser(auth.getName());
        registerReservation.setUserId(user.getId());

        return ResponseEntity.ok(reservationService.register(restaurantId, registerReservation));
    }

    //예약 조회 (시간 사용 o)
    @GetMapping("/query/{restaurantId}")
    public ResponseEntity<List<ReservationDto>> getByTimePeriod(
        @PathVariable("restaurantId") Long restaurantId,
        @RequestParam(value = "startTime") String startTime,
        @RequestParam(value = "endTime") String endTime
    ){
        return ResponseEntity.ok(reservationService.getTimePeriod(restaurantId,startTime,endTime));
    }

    //해당 예약 조회 (시간 사용 x)
    @GetMapping("/restaurantId")
    public ResponseEntity<List<ReservationDto>> getRestaurantId(
        @PathVariable("restaurantId") Long restaurantId
    ){
        return ResponseEntity.ok(reservationService.getByRestaurantId(restaurantId));
    }

    //해당 유저 예약 조회
    @GetMapping("{userId}")
    public ResponseEntity<List<ReservationDto>> getByUserId(
        @PathVariable("userId") Long userId
    ){
        return ResponseEntity.ok(reservationService.getAllByUserId(userId));
    }

    //예약 수정
    @PutMapping("/update/{reservationId}")
    public ResponseEntity<String> update(
        @RequestBody ReservationCommand.UpdateReservation updateReservation,
        @PathVariable("reservationId") Long reservationId, Authentication auth
    ){
        User user = userService.getUser(auth.getName());
        updateReservation.setUserId(user.getId());
        reservationService.updateReservation(reservationId ,updateReservation);

        return ResponseEntity.ok("수정 완료");
    }

    //예약 삭제
    @PutMapping("/delete/{reservationId}")
    public ResponseEntity<String> deleteReservation(
        @PathVariable("reservationId") Long reservationId
    ){
        reservationService.deleteReservation(reservationId);

        return ResponseEntity.ok("삭제 완료");
    }




}