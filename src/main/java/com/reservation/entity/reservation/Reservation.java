package com.reservation.entity.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reservation.entity.user.User;
import com.reservation.type.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @CreatedDate
    private String time;

    private LocalDateTime createdAt;
    private String place;
    private String name;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @PrePersist
    public void onPrePersist() {
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
}
}

