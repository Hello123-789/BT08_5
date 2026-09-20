package vn.iotstar.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.ProductImage;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Tạo hoặc Cập nhật Icon cho các Danh Mục
        Category cVot = categoryRepository.findByCategoryName("Vợt Cầu Lông").map(cat -> {
            cat.setIcon("vot_cau_long.png");
            return categoryRepository.save(cat);
        }).orElseGet(() -> {
            Category cat = new Category();
            cat.setCategoryName("Vợt Cầu Lông");
            cat.setIcon("vot_cau_long.png");
            return categoryRepository.save(cat);
        });

        Category cGiay = categoryRepository.findByCategoryName("Giày Cầu Lông").map(cat -> {
            cat.setIcon("giay_cau_long.jpg");
            return categoryRepository.save(cat);
        }).orElseGet(() -> {
            Category cat = new Category();
            cat.setCategoryName("Giày Cầu Lông");
            cat.setIcon("giay_cau_long.jpg");
            return categoryRepository.save(cat);
        });

        Category cPhuKien = categoryRepository.findByCategoryName("Phụ Kiện Cầu Lông").map(cat -> {
            cat.setIcon("phu_kien.jpg");
            return categoryRepository.save(cat);
        }).orElseGet(() -> {
            Category cat = new Category();
            cat.setCategoryName("Phụ Kiện Cầu Lông");
            cat.setIcon("phu_kien.jpg");
            return categoryRepository.save(cat);
        });

        // 2. Chèn 20 Vợt Cầu Lông
        createProductIfNotFound("Yonex Astrox 100 ZZ", "Yonex", "4500000", 10, "astrox100zz.jpg", "Vợt cầu lông cao cấp Yonex Astrox 100 ZZ.", cVot);
        createProductIfNotFound("Yonex Astrox 88D Pro", "Yonex", "4200000", 12, "astrox88dpro.jpg", "Vợt cầu lông Yonex Astrox 88D Pro.", cVot);
        createProductIfNotFound("Yonex Astrox 88S Pro", "Yonex", "4100000", 10, "astrox88spro.jpg", "Vợt cầu lông Yonex Astrox 88S Pro.", cVot);
        createProductIfNotFound("Yonex Nanoflare 1000 Z", "Yonex", "4600000", 8, "nanoflare1000z.jpg", "Vợt cầu lông tốc độ cao Yonex Nanoflare 1000 Z.", cVot);
        createProductIfNotFound("Yonex Nanoflare 800 Pro", "Yonex", "3900000", 15, "nanoflare800pro.jpg", "Vợt cầu lông Yonex Nanoflare 800 Pro.", cVot);
        createProductIfNotFound("Yonex Arcsaber 11 Pro", "Yonex", "4000000", 10, "arcsaber11pro.jpg", "Vợt cầu lông Yonex Arcsaber 11 Pro.", cVot);
        createProductIfNotFound("Victor Thruster Ryuga II", "Victor", "3800000", 9, "ryuga2.jpg", "Vợt cầu lông Victor Thruster Ryuga II.", cVot);
        createProductIfNotFound("Victor Auraspeed 100X", "Victor", "3600000", 11, "auraspeed100x.jpg", "Vợt cầu lông Victor Auraspeed 100X.", cVot);
        createProductIfNotFound("Victor Auraspeed 90K", "Victor", "3500000", 14, "auraspeed90k.jpg", "Vợt cầu lông Victor Auraspeed 90K.", cVot);
        createProductIfNotFound("Victor Thruster K Falcon", "Victor", "3200000", 10, "thrusterkfalcon.jpg", "Vợt cầu lông Victor Thruster K Falcon.", cVot);
        createProductIfNotFound("Li-Ning Axforce 100", "Li-Ning", "4300000", 8, "axforce100.jpg", "Vợt cầu lông Li-Ning Axforce 100.", cVot);
        createProductIfNotFound("Li-Ning Axforce 80", "Li-Ning", "3900000", 10, "axforce80.jpg", "Vợt cầu lông Li-Ning Axforce 80.", cVot);
        createProductIfNotFound("Li-Ning Axforce 75", "Li-Ning", "3700000", 12, "axforce75.jpg", "Vợt cầu lông Li-Ning Axforce 75.", cVot);
        createProductIfNotFound("Li-Ning Tectonic 9", "Li-Ning", "3500000", 9, "tectonic9.jpg", "Vợt cầu lông Li-Ning Tectonic 9.", cVot);
        createProductIfNotFound("Mizuno Fortius 11 Power", "Mizuno", "3300000", 10, "fortius11power.jpg", "Vợt cầu lông Mizuno Fortius 11 Power.", cVot);
        createProductIfNotFound("Mizuno Fortius 10 Power", "Mizuno", "3100000", 12, "fortius10power.jpg", "Vợt cầu lông Mizuno Fortius 10 Power.", cVot);
        createProductIfNotFound("Victor DriveX 9X", "Victor", "3000000", 15, "drivex9x.jpg", "Vợt cầu lông Victor DriveX 9X.", cVot);
        createProductIfNotFound("Victor Auraspeed 80X", "Victor", "2900000", 10, "auraspeed80x.jpg", "Vợt cầu lông Victor Auraspeed 80X.", cVot);
        createProductIfNotFound("Yonex Duora Z Strike", "Yonex", "3400000", 7, "duorazstrike.jpg", "Vợt cầu lông Yonex Duora Z Strike.", cVot);
        createProductIfNotFound("Yonex Voltric Z Force II", "Yonex", "3700000", 6, "voltriczforce2.jpg", "Vợt cầu lông Yonex Voltric Z Force II.", cVot);

        // 3. Chèn 6 Giày Cầu Lông
        createProductIfNotFound("Yonex Power Cushion 65 Z3", "Yonex", "3200000", 10, "65z3.jpg", "Giày cầu lông Yonex Power Cushion 65 Z3.", cGiay);
        createProductIfNotFound("Yonex Power Cushion 88 Dial", "Yonex", "3500000", 8, "88dial.jpg", "Giày cầu lông Yonex Power Cushion 88 Dial.", cGiay);
        createProductIfNotFound("Victor P9200 III", "Victor", "3000000", 10, "p9200iii.jpg", "Giày cầu lông Victor P9200 III.", cGiay);
        createProductIfNotFound("Victor A970 Nitro Lite", "Victor", "2800000", 12, "a970.jpg", "Giày cầu lông Victor A970 Nitro Lite.", cGiay);
        createProductIfNotFound("Li-Ning Ranger Lite", "Li-Ning", "2200000", 10, "rangerlite.jpg", "Giày cầu lông Li-Ning Ranger Lite.", cGiay);
        createProductIfNotFound("Mizuno Wave Claw Neo", "Mizuno", "2900000", 8, "waveclawneo.jpg", "Giày cầu lông Mizuno Wave Claw Neo.", cGiay);

        // 4. Chèn 6 Phụ Kiện
        createProductIfNotFound("Yonex AC102EX Power Cushion Grip", "Yonex", "80000", 50, "ac102ex.jpg", "Quấn cán vợt Yonex AC102EX.", cPhuKien);
        createProductIfNotFound("Yonex Aerosensa 50", "Yonex", "650000", 30, "aerosensa50.jpg", "Cầu lông Yonex Aerosensa 50.", cPhuKien);
        createProductIfNotFound("Yonex AC110EX Towel Grip", "Yonex", "90000", 40, "ac110ex.jpg", "Quấn cán khăn Yonex AC110EX.", cPhuKien);
        createProductIfNotFound("Victor GR262", "Victor", "70000", 45, "gr262.jpg", "Quấn cán Victor GR262.", cPhuKien);
        createProductIfNotFound("Li-Ning GP20", "Li-Ning", "75000", 40, "gp20.jpg", "Quấn cán vợt Li-Ning GP20.", cPhuKien);
        createProductIfNotFound("Yonex 3D Power Cushion Socks", "Yonex", "120000", 35, "powersocks.jpg", "Vớ cầu lông Yonex 3D Power Cushion.", cPhuKien);
    }

    private void createProductIfNotFound(String name, String brand, String price, Integer quantity, String imageName, String description, Category category) {
        if (productRepository.findByName(name).isPresent()) {
            return;
        }

        Product product = Product.builder()
                .name(name)
                .brand(brand)
                .price(new BigDecimal(price))
                .quantity(quantity)
                .description(description)
                .category(category)
                .build();

        if (imageName != null && !imageName.isEmpty()) {
            ProductImage img = ProductImage.builder()
                    .product(product)
                    .imageUrl(imageName)
                    .primary(true)
                    .displayOrder(0)
                    .createdAt(LocalDateTime.now())
                    .build();
            product.getImages().add(img);
        }

        productRepository.save(product);
    }
}
