package com.reservation.entity.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reservation.dto.reservation.ReservationCommand;
import com.reservation.dto.reservation.util.TimeParsingUtils;
import com.reservation.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

@Entity
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

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // 회원의 계정이 삭제되었을 경우 같이 삭제
    private User user;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "res_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // 매장이 삭제되었을 경우 같이 삭제
    private Restaurant restaurant;

    @CreatedDate
    private String createdTime; //예약한 시간

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime; //예약 시작

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime; //예약 종료

    private String place; //상호명

    private String name; //예약자명


    public void update(ReservationCommand.UpdateReservation updateReservation) {
        this.startTime = TimeParsingUtils.formatterLocalDateTime(updateReservation.getStartTime());
        this.endTime = TimeParsingUtils.formatterLocalDateTime(updateReservation.getEndTime());
    }

    @PrePersist
    public void onPrePersist() {
        this.createdTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
}


}

