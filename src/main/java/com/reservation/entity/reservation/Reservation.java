package com.reservation.entity.reservation;
import com.reservation.entity.user.User;
import com.reservation.type.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Reservation {
    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

//    @OneToOne
//    @JoinColumn(name = "review_id")
//    @JsonManagedReference
//    private Review review;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    private LocalDateTime time;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

