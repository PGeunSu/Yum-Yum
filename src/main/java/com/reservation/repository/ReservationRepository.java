package com.reservation.repository;

import com.reservation.entity.reservation.Reservation;
import feign.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByUserIdOrderByTimeDesc(Long userId, Pageable pageable);

    @Query("select r from Reservation r left join fetch r.restaurant where" +
        "r.restaurant.id = :restaurantId or r.startTime = :sTime or r.endTime = :eTime")
    List<Reservation> findByRestaurantIdAndTime(
        @Param("restaurantId") Long restaurantId,
        @Param("sTime") LocalDateTime sTime,
        @Param("sTime") LocalDateTime eTime);

    @Query("select r from Reservation r left join fetch r.restaurant where r.restaurant.id = :restaurantId " +
        "and r.startTime >= :sTime and r.endTime <= :eTime")
    List<Reservation> findByRestaurantIdAndTimePeriod(
        @Param("restaurantId") Long restaurantId,
        @Param("sTime") LocalDateTime sTime,
        @Param("sTime") LocalDateTime eTime);

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByRestaurantId(Long restaurantId);
}
