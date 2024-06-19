package com.reservation.dto.reservation;

import com.reservation.entity.reservation.Restaurant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantList {

    private Long id;
    private String post;
    private String address;
    private String newAdress;
    private String tell;
    private String time;

    public void RestaurantList(Long id, String post, String address,
        String newAdress, String tell, String time) {
        this.id = id;
        this.post = post;
        this.address = address;
        this.newAdress = newAdress;
        this.tell = tell;
        this.time = time;
    }

    public static RestaurantList fromEntity(Restaurant restaurant){
        return RestaurantList.builder()
            .post(restaurant.getPost())
            .address(restaurant.getAddress())
            .newAdress(restaurant.getNewAdress())
            .build();
    }

}
