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
    @Column(name = "store_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;

    @Column(name = "store_name")
    private String name;

    @OneToMany(mappedBy = "store")
    private List<Reservation> reservationList = new ArrayList<>();

    private String location;
    private String description;

    private double x;
    private double y;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
