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
}