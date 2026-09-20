package vn.iotstar.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Product;

public interface ProductService {
    Page<ProductDTO> findAll(String keyword, int page, int size);
    ProductDTO findById(Long id);
    ProductDTO create(ProductDTO dto);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);

    List<Product> findAllEntities();
    Page<Product> findEntities(String keyword, int page, int size);
    Optional<Product> findEntityById(Long id);
    Optional<Product> findByName(String name);
    Product saveEntity(Product product);
    void deleteEntity(Product product);

    String saveImage(MultipartFile file) throws IOException;
    void saveImages(Product product, List<MultipartFile> files) throws IOException;
    Long deleteImage(Long imageId);
    void deleteFile(String fileName);
}
