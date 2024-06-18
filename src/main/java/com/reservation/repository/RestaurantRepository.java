package com.reservation.repository;

import com.reservation.entity.reservation.Restaurant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByRestaurant(String POST_SJ);


}
