package com.reservation.dto.reservation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantList {

    private Long id;
    private String post;
    private String address;
    private String newadress;
    private String tell;
    private String time;

    public void RestaurantList(Long id, String post, String address,
        String newadress, String tell, String time) {
        this.id = id;
        this.post = post;
        this.address = address;
        this.newadress = newadress;
        this.tell = tell;
        this.time = time;
    }
}
