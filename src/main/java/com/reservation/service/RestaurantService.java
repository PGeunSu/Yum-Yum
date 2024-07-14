package com.reservation.service;

import com.reservation.dto.restaurant.RestaurantDto;
import com.reservation.entity.reservation.Restaurant;
import com.reservation.exception.ErrorCode;
import com.reservation.exception.Exception;
import com.reservation.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    //상점명으로 상점정보찾기
    public RestaurantDto findById(Long id) {
        Restaurant findRestaurant = restaurantRepository.findById(id)
            //restaurantR에서 rest의 r은 소문자.
            .orElseThrow(() -> new Exception(ErrorCode.STORE_NOT_FOUND));
        return RestaurantDto.fromEntity(findRestaurant);
    }
}