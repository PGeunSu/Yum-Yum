package com.reservation.entity.reservation;

import com.reservation.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Restaurant {
    @Id
    @GeneratedValue
//    @Column(name = "res_id")
    private Long id;

    @ManyToOne
//    @JoinColumn(name = "user_id")
    private User user;

    //    @Column(name = "res_name")
    private String name;

    @OneToMany(mappedBy = "restaurant")
    private List<Reservation> reservationList = new ArrayList<>();

    private String address;
    private String openTime;
    private String breakTime;
    private String closeTime;

//    private double x;
//    private double y;
//
//    @CreatedDate
//    private LocalDateTime createdAt;

//    private LocalDateTime updatedAt;
}