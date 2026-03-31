package com.kidstore.DoubleN_kidstore.repository;

import com.kidstore.DoubleN_kidstore.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
}