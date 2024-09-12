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
public class ColorEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Color")
    private String color;

    @ManyToOne
    @JoinColumn(name = "Category_id")
    private CategoryEntity categoryEntity;
}
