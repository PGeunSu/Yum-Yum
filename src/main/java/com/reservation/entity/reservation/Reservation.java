package com.reservation.entity.reservation;

import com.reservation.entity.user.User;
import com.reservation.type.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rsv_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")//이름 논의 필요
    private User user;

    @ManyToOne
    @JoinColumn(name = "res_id")//이름 논의 필요
    private Restaurant restaurant;

    private LocalDateTime time;
    private String place;
    private String name;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    //    private LocalDateTime time;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

