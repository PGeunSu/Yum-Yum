package com.reservation.dto.reservation;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class RestaurantListQuery {
        private String post;
//        private RestaurantSortType restaurantSortType;
//
//        private double lat;
//        private double lnt;

        public Object getSortType() {
            return null;
        }
    }
