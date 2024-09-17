package com.ecommerce.entity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private MainCategoryEntity mainCategoryEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "model_id")
    private ModelEntity modelEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "color_id")
    private ColorEntity colorEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ram_id")
    private RamEntity ramEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "internal_storage_id")
    private InternalStorageEntity internalStorageEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "brand_id")
    private BrandEntity brandEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sim_slot_id")
    private SimSlotEntity simSlotEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "battery_capacity_id")
    private BatteryCapacityEntity batteryCapacityEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "screen_size_id")
    private ScreenSizeEntity screenSizeEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "processor_id")
    private ProcessorEntity processorEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "network_id")
    private NetworkEntity networkEntity;

    public ProductEntity(MainCategoryEntity categoryEntity, ModelEntity modelEntity, List<ColorEntity> colorEntityList, List<RamEntity> ramEntityList, List<InternalStorageEntity> internalStorageList, BrandEntity brandEntity, SimSlotEntity simSlotEntity, BatteryCapacityEntity batteryEntity, ScreenSizeEntity screenSizeEntity, ProcessorEntity processorEntity, NetworkEntity networkEntity) {
    }

    public ProductEntity(MainCategoryEntity mainCategoryEntity, ModelEntity modelEntity, BrandEntity brandEntity, SimSlotEntity simSlotEntity, BatteryCapacityEntity batteryCapacityEntity, ScreenSizeEntity screenSizeEntity, ProcessorEntity processorEntity, NetworkEntity networkEntity) {
        this.mainCategoryEntity = mainCategoryEntity;
        this.modelEntity = modelEntity;
        this.brandEntity = brandEntity;
        this.simSlotEntity = simSlotEntity;
        this.batteryCapacityEntity = batteryCapacityEntity;
        this.screenSizeEntity = screenSizeEntity;
        this.processorEntity = processorEntity;
        this.networkEntity = networkEntity;
    }
}
