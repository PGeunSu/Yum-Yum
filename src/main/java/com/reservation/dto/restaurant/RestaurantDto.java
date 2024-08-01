package com.reservation.dto.restaurant;

import com.reservation.entity.reservation.Restaurant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantDto {

    private Long id;
    private String post;
    private String address;
    private String newAddress;
    private String tell;
    private String time;

    public static RestaurantDto fromEntity(Restaurant restaurant){
        return RestaurantDto.builder()
            .id(restaurant.getId())
            .post(restaurant.getPost())
            .address(restaurant.getAddress())
            .newAddress(restaurant.getNewAddress())
            .build();
    }

}