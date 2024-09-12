package com.ecommerce.entity.requestDto;

import com.ecommerce.entity.entity.*;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExcelRequestDto {

    private CategoryEntity category;
    private ModelEntity model;
    private CategoryEntity subCategory;
    private InternalStorageEntity internalStorage;
    private PriceEntity price;
    private RamEntity ram;
    private ColorEntity color;
}
