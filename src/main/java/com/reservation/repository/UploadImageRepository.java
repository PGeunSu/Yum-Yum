package com.reservation.repository;

import com.reservation.entity.board.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {
}
