package com.ecommerce.demo.service.impl;

import com.ecommerce.demo.service.ExcelService;
import com.ecommerce.demo.utill.Utilities;
import com.ecommerce.entity.entity.*;
import com.ecommerce.entity.requestDto.ExcelRequestDto;
import com.ecommerce.repository.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;
    private final CategoryRepository categoryRepository;
    private final ModelRepository modelRepository;
    private final RamRepository ramRepository;
    private final ColorRepository colorRepository;
    private final InternalStorageRepository internalStorageRepository;
    private final Utilities utilities;

    @Override
    public void readFileAndSave(MultipartFile multipartFile) throws IOException {

        Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        UserEntity currentUser = utilities.currentUser();

        for (Row row : sheet) {
//            skip header
            if (row.getRowNum() != 0) {
                String category = row.getCell(0).getStringCellValue();
                String subCategory = row.getCell(1).getStringCellValue();
                String model = row.getCell(2).getStringCellValue();
                String ram = row.getCell(3).getStringCellValue();
                String internalStorage = row.getCell(4).getStringCellValue();
                String color = row.getCell(5).getStringCellValue();
                int price = (int) row.getCell(6).getNumericCellValue();


                // find in category master if not then save
                var categoryEntity = this.categoryRepository.findByCategoryName(category).orElseGet(
                        () -> saveCategoryEntity(category, currentUser));

                var subCategoryEntity = this.categoryRepository.findByCategoryName(subCategory);


                var priceEntity = this.priceRepository.findByAmount(price).orElseGet(
                        () -> savepriceEntity(price, currentUser));

                var modelEntity = this.modelRepository.findByModalName(model).orElseGet(
                        () -> saveModelEntity(model, categoryEntity, currentUser));

                var colorEntity = this.colorRepository.findByColor(color).orElseGet(
                        () -> saveColorEntity(color, categoryEntity, currentUser));

                var internalStorageEntity = this.internalStorageRepository.findByInternalStorage(internalStorage).orElseGet(
                        () -> saveInternalStorage(internalStorage, categoryEntity, currentUser));

                var ramEntity = this.ramRepository.findByRam(ram).orElseGet(
                        () -> saveRamEntity(ram, categoryEntity, currentUser));


                // check in product master table for above fetched entities if exists skip if not save and next
                ProductEntity productEntity = ProductEntity.builder()
                        .categoryEntity(categoryEntity)
                        .colorEntity(colorEntity)
                        .modelEntity(modelEntity)
                        .internalStorageEntity(internalStorageEntity)
                        .ramEntity(ramEntity)
                        .subCategoryEntity(categoryEntity)
                        .priceEntity(priceEntity)
                        .build();
                productEntity.setCreatedBy(currentUser);
                productEntity.setUpdatedBy(currentUser);
                ExcelRequestDto excelRequestDto = new ExcelRequestDto(categoryEntity, modelEntity, categoryEntity, internalStorageEntity, priceEntity, ramEntity, colorEntity);
                var productEntitySave = this.productRepository.findByProduct(excelRequestDto).orElseGet(() -> saveOrCreateProductEntity(productEntity));
            }

        }

    }

    private ProductEntity saveOrCreateProductEntity(ProductEntity productEntity) {
        return this.productRepository.save(productEntity);
    }

    private RamEntity saveRamEntity(String ram, CategoryEntity categoryEntity, UserEntity currentUser) {
        RamEntity ramEntity = RamEntity.builder()
                .ram(ram)
                .categoryEntity(categoryEntity)
                .build();
        ramEntity.setCreatedBy(currentUser);
        ramEntity.setUpdatedBy(currentUser);
        return this.ramRepository.save(ramEntity);
    }

    private InternalStorageEntity saveInternalStorage(String internalStorage, CategoryEntity categoryEntity, UserEntity currentUser) {
        InternalStorageEntity internalStorageEntity = InternalStorageEntity.builder()
                .internalStorage(internalStorage)
                .categoryEntity(categoryEntity)
                .build();
        internalStorageEntity.setCreatedBy(currentUser);
        internalStorageEntity.setUpdatedBy(currentUser);
        return this.internalStorageRepository.save(internalStorageEntity);
    }

    private ColorEntity saveColorEntity(String color, CategoryEntity categoryEntity, UserEntity currentUser) {
        ColorEntity colorEntity = ColorEntity.builder()
                .color(color)
                .categoryEntity(categoryEntity)
                .build();
        colorEntity.setCreatedBy(currentUser);
        colorEntity.setUpdatedBy(currentUser);
        return this.colorRepository.save(colorEntity);
    }

    private ModelEntity saveModelEntity(String model, CategoryEntity categoryEntity, UserEntity currentUser) {
        ModelEntity modelEntity = ModelEntity.builder()
                .modalName(model)
                .categoryEntity(categoryEntity).build();
        modelEntity.setCreatedBy(currentUser);
        modelEntity.setUpdatedBy(currentUser);
        return this.modelRepository.save(modelEntity);


    }

    private PriceEntity savepriceEntity(int price, UserEntity currentUser) {
        PriceEntity priceEntity = PriceEntity.builder()
                .amount(price)
                .build();
        priceEntity.setCreatedBy(currentUser);
        priceEntity.setUpdatedBy(currentUser);
        return this.priceRepository.save(priceEntity);
    }

    // save category entity
    private CategoryEntity saveCategoryEntity(String categoryName, UserEntity currentUser) {
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .categoryName(categoryName)
                .build();
        categoryEntity.setCreatedBy(currentUser);
        categoryEntity.setUpdatedBy(currentUser);
        // save into database
        return this.categoryRepository.save(categoryEntity);
    }

}
