package com.ecommerce.repository.repository;

import com.ecommerce.entity.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity,Long> {

    Optional<BrandEntity> findByBrandName(String brandName);
}
