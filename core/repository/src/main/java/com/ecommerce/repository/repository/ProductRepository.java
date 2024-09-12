package com.ecommerce.repository.repository;

import com.ecommerce.entity.entity.ProductEntity;
import com.ecommerce.entity.requestDto.ExcelRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query("""
            SELECT p FROM ProductEntity p WHERE p.categoryEntity = :#{#excelRequestDto.category} 
            AND p.subCategoryEntity = :#{#excelRequestDto.category} 
            AND p.modelEntity = :#{#excelRequestDto.model} AND p.colorEntity = :#{#excelRequestDto.color} 
            AND p.ramEntity = :#{#excelRequestDto.ram} AND p.internalStorageEntity = :#{#excelRequestDto.internalStorage} 
            AND p.priceEntity = :#{#excelRequestDto.price}
            """)
    Optional<ProductEntity> findByProduct(ExcelRequestDto excelRequestDto);

}
