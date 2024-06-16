package com.reservation.dto.reservation;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;


public class RestaurantList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String POST_SJ;
    private String ADDRESS;
    private String NEW_ADDRESS;
    private String CMMN_TELNO;
    private String CMMN_USE_TIME;

    public RestaurantList(Long id, String POST_SJ, String ADDRESS,
        String NEW_ADDRESS, String CMMN_TELNO, String CMMN_USE_TIME) {
        this.id = id;
        this.POST_SJ = POST_SJ;
        this.ADDRESS = ADDRESS;
        this.NEW_ADDRESS = NEW_ADDRESS;
        this.CMMN_TELNO = CMMN_TELNO;
        this.CMMN_USE_TIME = CMMN_USE_TIME;
    }
}
