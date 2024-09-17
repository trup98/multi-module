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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;
    private final MainCategoryRepository categoryRepository;
    private final ModelRepository modelRepository;
    private final RamRepository ramRepository;
    private final ColorRepository colorRepository;
    private final InternalStorageRepository internalStorageRepository;
    private final Utilities utilities;
    private final BrandRepository brandRepository;
    private final NetworkRepository networkRepository;
    private final SimSlotRepository simSlotRepository;
    private final ScreenSizeRepository screenSizeRepository;
    private final BatteryCapacityRepository batteryCapacityRepository;
    private final ProcessorRepository processorRepository;

    /**
     * @param multipartFile <>this method read excel and store the data</>
     * @throws IOException
     */
    @Override
    public void readFileAndSave(MultipartFile multipartFile) throws IOException {

        Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        UserEntity currentUser = utilities.currentUser();

        for (Row row : sheet) {
//            skip header
            if (row.getRowNum() != 0) {
                String category = row.getCell(0).getStringCellValue();
                String brand = row.getCell(1).getStringCellValue();
                String model = row.getCell(2).getStringCellValue();
                String colorNames = row.getCell(3).getStringCellValue();
                String rams = row.getCell(4).getStringCellValue();
                String internalStorages = row.getCell(5).getStringCellValue();
                String network = row.getCell(6).getStringCellValue();
                String simSlot = row.getCell(7).getStringCellValue();
                String screenSize = row.getCell(8).getStringCellValue();
                String battery = row.getCell(9).getStringCellValue();
                String processor = row.getCell(10).getStringCellValue();


                System.out.println("category = " + category);
                System.out.println("brand = " + brand);
                System.out.println("model = " + model);
                System.out.println("colorNames = " + colorNames.toString());
                System.out.println("rams = " + rams.toString());
                System.out.println("internalStorages = " + internalStorages.toString());
                System.out.println("network = " + network);
                System.out.println("simSlot = " + simSlot);
                System.out.println("screenSize = " + screenSize);
                System.out.println("battery = " + battery);
                System.out.println("processor = " + processor);

                // find in category master if not then save
                var categoryEntity = this.categoryRepository.findByCategoryName(category).orElseGet(
                        () -> saveOrCreateCategoryEntity(category, currentUser));

                var brandEntity = this.brandRepository.findByBrandName(brand).orElseGet(
                        () -> saveOrCreateBrandEntity(brand, currentUser, categoryEntity));

                var modelEntity = this.modelRepository.findByModalName(model).orElseGet(
                        () -> saveOrCreateModelEntity(model, categoryEntity, currentUser));

                var colorEntityList = saveOrCreateColorEntity(colorNames, currentUser, categoryEntity);

                var ramEntityList = saveOrCreateRamEntity(rams, currentUser, categoryEntity);

                var internalStorageList = saveOrCreateInternalStorageEntities(internalStorages, currentUser, categoryEntity);

                var networkEntity = this.networkRepository.findByNetworkType(network).orElseGet(
                        () -> saveOrCreateNetworkEntity(network, categoryEntity, currentUser));

                var simSlotEntity = this.simSlotRepository.findBySimSlotType(simSlot).orElseGet(
                        () -> saveOrCreateSimSlotTypeEntity(simSlot, categoryEntity, currentUser));

                var screenSizeEntity = this.screenSizeRepository.findByScreenSize(screenSize).orElseGet(
                        () -> saveOrCreateScreenSizeEntity(screenSize, categoryEntity, currentUser));

                var batteryEntity = this.batteryCapacityRepository.findByBatteryCapacity(battery).orElseGet(
                        () -> saveOrCreateBattery(battery, categoryEntity, currentUser));

                var processorEntity = this.processorRepository.findByProcessorName(processor).orElseGet(
                        () -> saveOrCreateProcessor(processor, categoryEntity, currentUser));


//                for (ColorEntity colorEntity : colorEntityList) {
//                    for (RamEntity ramEntity : ramEntityList) {
//                        for (InternalStorageEntity internalStorageEntity : internalStorageList) {
//
//                            ExcelRequestDto excelRequestDto = new ExcelRequestDto(categoryEntity, modelEntity, colorEntity, ramEntity, internalStorageEntity, brandEntity, simSlotEntity, batteryEntity, screenSizeEntity, processorEntity, networkEntity);
//                            productRepository.findByAttributes(excelRequestDto).orElse(
//
//
//                            );
//
//                        }
//                    }
//                }\

                List<ProductEntity> productEntities = new ArrayList<>();
                int totalSize = internalStorageList.size() * ramEntityList.size() * colorEntityList.size();
                int colourSize = internalStorageList.size() * ramEntityList.size();
                int ramSize = colorEntityList.size() * internalStorageList.size();
                int internalSize = colorEntityList.size() * ramEntityList.size();
                int colorIndex = 0;
                int ramIndex = 0;
                int internalIndex = 0;
                int ramItems = 0, colorItems = 0, internalItems = 0;
                for (int i = 0; i < totalSize; i++) {
                    if (ramItems == ramSize) {
                        ramIndex++;
                        ramItems = 0;
                    }
                    if (colorItems == colourSize) {
                        colorIndex++;
                        colorItems = 0;
                    }
                    if (internalItems == internalSize) {
                        internalIndex++;
                        internalItems = 0;
                    }
                    ProductEntity productEntity = new ProductEntity(categoryEntity, modelEntity, brandEntity, simSlotEntity, batteryEntity, screenSizeEntity, processorEntity, networkEntity);
                    productEntity.setColorEntity(colorEntityList.get(colorIndex));
                    productEntity.setInternalStorageEntity(internalStorageList.get(internalIndex));
                    productEntity.setRamEntity(ramEntityList.get(ramIndex));
                    productEntity.setUpdatedBy(currentUser);
                    productEntity.setCreatedBy(currentUser);
                    ramItems++;
                    colorItems++;
                    internalItems++;

                    ExcelRequestDto excelRequestDto = new ExcelRequestDto(categoryEntity, modelEntity, productEntity.getColorEntity(), productEntity.getRamEntity(), productEntity.getInternalStorageEntity(), brandEntity, simSlotEntity, batteryEntity, screenSizeEntity, processorEntity, networkEntity);
                    var entity = this.productRepository.findByAttributes(excelRequestDto);
                    if(!entity.isPresent())
                            productEntities.add(productEntity);
                }
                productRepository.saveAll(productEntities);
            }
        }
    }

    private List<ColorEntity> saveOrCreateColorEntity(String colorNames, UserEntity currentUser, MainCategoryEntity categoryEntity) {
        List<String> items = Arrays.asList(colorNames.split("\\s*,\\s*"));
        List<ColorEntity> colorEntityList = new ArrayList<>();
        items.forEach(r -> {
            var colorE = this.colorRepository.findByColor(r);
            if (colorE.isPresent()) {
                colorEntityList.add(colorE.get());
            } else {
                ColorEntity colorEntity = new ColorEntity();
                colorEntity.setMainCategoryEntity(categoryEntity);
                colorEntity.setCreatedBy(currentUser);
                colorEntity.setUpdatedBy(currentUser);
                colorEntity.setColor(r);
                colorEntityList.add(colorEntity);
            }
        });
        this.colorRepository.saveAll(colorEntityList);
        return colorEntityList;
    }

    private List<RamEntity> saveOrCreateRamEntity(String ramNames, UserEntity currentUser, MainCategoryEntity categoryEntity) {
        List<String> items = Arrays.asList(ramNames.split("\\s*,\\s*"));
        List<RamEntity> ramEntityList = new ArrayList<>();
        items.forEach(r -> {
            var ramLists = this.ramRepository.findByRam(r);
            if (ramLists.isPresent()) {
                ramEntityList.add(ramLists.get());
            } else {
                RamEntity ramEntity = new RamEntity();
                ramEntity.setMainCategoryEntity(categoryEntity);
                ramEntity.setCreatedBy(currentUser);
                ramEntity.setUpdatedBy(currentUser);
                ramEntity.setRam(r);
                ramEntityList.add(ramEntity);
            }
        });
        this.ramRepository.saveAll(ramEntityList);
        return ramEntityList;
    }

    private List<InternalStorageEntity> saveOrCreateInternalStorageEntities(String internalStorageNames, UserEntity currentUser, MainCategoryEntity categoryEntity) {
        List<String> items = Arrays.asList(internalStorageNames.split("\\s*,\\s*"));
        List<InternalStorageEntity> internalStorageEntities = new ArrayList<>();
        items.forEach(r -> {
            var internalStorageLists = this.internalStorageRepository.findByInternalStorage(r);
            if (internalStorageLists.isPresent()) {
                internalStorageEntities.add(internalStorageLists.get());
            } else {

                InternalStorageEntity internalStorageEntity = new InternalStorageEntity();
                internalStorageEntity.setMainCategoryEntity(categoryEntity);
                internalStorageEntity.setCreatedBy(currentUser);
                internalStorageEntity.setUpdatedBy(currentUser);
                internalStorageEntity.setInternalStorage(r);
                internalStorageEntities.add(internalStorageEntity);
            }
        });
        this.internalStorageRepository.saveAll(internalStorageEntities);
        return internalStorageEntities;
    }


    private ProcessorEntity saveOrCreateProcessor(String processor, MainCategoryEntity categoryEntity, UserEntity currentUser) {
        ProcessorEntity processorEntity = ProcessorEntity.builder()
                .processorName(processor)
                .mainCategoryEntity(categoryEntity)
                .build();
        processorEntity.setCreatedBy(currentUser);
        processorEntity.setUpdatedBy(currentUser);
        return this.processorRepository.save(processorEntity);
    }

    private BatteryCapacityEntity saveOrCreateBattery(String battery, MainCategoryEntity categoryEntity, UserEntity currentUser) {
        BatteryCapacityEntity batteryCapacityEntity = BatteryCapacityEntity.builder()
                .batteryCapacity(battery)
                .mainCategoryEntity(categoryEntity)
                .build();
        batteryCapacityEntity.setCreatedBy(currentUser);
        batteryCapacityEntity.setUpdatedBy(currentUser);
        return this.batteryCapacityRepository.save(batteryCapacityEntity);
    }

    private ScreenSizeEntity saveOrCreateScreenSizeEntity(String screenSize, MainCategoryEntity categoryEntity, UserEntity currentUser) {
        ScreenSizeEntity screenSizeEntity = ScreenSizeEntity.builder()
                .screenSize(screenSize)
                .mainCategoryEntity(categoryEntity)
                .build();
        screenSizeEntity.setCreatedBy(currentUser);
        screenSizeEntity.setUpdatedBy(currentUser);
        return this.screenSizeRepository.save(screenSizeEntity);
    }

    private SimSlotEntity saveOrCreateSimSlotTypeEntity(String simSlot, MainCategoryEntity categoryEntity, UserEntity currentUser) {
        SimSlotEntity simSlotEntity = SimSlotEntity.builder()
                .simSlotType(simSlot)
                .mainCategoryEntity(categoryEntity)
                .build();
        simSlotEntity.setCreatedBy(currentUser);
        simSlotEntity.setUpdatedBy(currentUser);
        return this.simSlotRepository.save(simSlotEntity);

    }

    private NetworkEntity saveOrCreateNetworkEntity(String network, MainCategoryEntity categoryEntity, UserEntity currentUser) {
        NetworkEntity networkEntity = NetworkEntity.builder()
                .networkType(network)
                .mainCategoryEntity(categoryEntity)
                .build();
        networkEntity.setCreatedBy(currentUser);
        networkEntity.setUpdatedBy(currentUser);
        return this.networkRepository.save(networkEntity);
    }

    private BrandEntity saveOrCreateBrandEntity(String brand, UserEntity currentUser, MainCategoryEntity categoryEntity) {
        BrandEntity brandEntity = BrandEntity.builder()
                .brandName(brand)
                .mainCategoryEntity(categoryEntity)
                .build();
        brandEntity.setCreatedBy(currentUser);
        brandEntity.setUpdatedBy(currentUser);
        return this.brandRepository.save(brandEntity);
    }

    private MainCategoryEntity saveOrCreateCategoryEntity(String categoryName, UserEntity currentUser) {
        MainCategoryEntity categoryEntity = MainCategoryEntity.builder()
                .categoryName(categoryName)
                .build();
        categoryEntity.setCreatedBy(currentUser);
        categoryEntity.setUpdatedBy(currentUser);
        // save into database
        return this.categoryRepository.save(categoryEntity);
    }

    private ModelEntity saveOrCreateModelEntity(String model, MainCategoryEntity categoryEntity, UserEntity currentUser) {
        ModelEntity modelEntity = ModelEntity.builder()
                .modalName(model)
                .mainCategoryEntity(categoryEntity).build();
        modelEntity.setCreatedBy(currentUser);
        modelEntity.setUpdatedBy(currentUser);
        return this.modelRepository.save(modelEntity);


    }

}
