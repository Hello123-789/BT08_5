package vn.iotstar.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Product;

@Component
public class ProductMapper {

    private final ProductImageMapper imageMapper;

    public ProductMapper(ProductImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    public ProductDTO toDTO(Product entity) {
        if (entity == null) {
            return null;
        }
        return ProductDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .description(entity.getDescription())
                .images(entity.getImages() != null ?
                        entity.getImages().stream()
                                .map(imageMapper::toDTO)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .brand(dto.getBrand())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .description(dto.getDescription())
                .build();
    }

    public void updateEntity(ProductDTO dto, Product entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setName(dto.getName());
        if (dto.getBrand() != null) {
            entity.setBrand(dto.getBrand());
        }
        entity.setPrice(dto.getPrice());
        entity.setQuantity(dto.getQuantity());
        entity.setDescription(dto.getDescription());
    }
}
