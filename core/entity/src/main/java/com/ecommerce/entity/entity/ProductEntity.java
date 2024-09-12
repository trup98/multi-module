package com.ecommerce.entity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Category_id")
    private CategoryEntity categoryEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Sub_Category_id")
    private CategoryEntity subCategoryEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Model_id")
    private ModelEntity modelEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Color_id")
    private ColorEntity colorEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Ram_id")
    private RamEntity ramEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Internal_Storage_id")
    private InternalStorageEntity internalStorageEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Price_id")
    private PriceEntity priceEntity;
}
