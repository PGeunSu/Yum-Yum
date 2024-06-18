package com.reservation.entity.reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurant")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String POST_SJ;
    private String ADDRESS;
    private String NEW_ADDRESS;
    private String CMMN_TELNO;
    private String CMMN_USE_TIME;

    public void RestaurantList(Long id, String POST_SJ, String ADDRESS,
        String NEW_ADDRESS, String CMMN_TELNO, String CMMN_USE_TIME) {
        this.id = id;
        this.POST_SJ = POST_SJ;
        this.ADDRESS = ADDRESS;
        this.NEW_ADDRESS = NEW_ADDRESS;
        this.CMMN_TELNO = CMMN_TELNO;
        this.CMMN_USE_TIME = CMMN_USE_TIME;
    }
}