package vn.iotstar.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.ProductImage;
import vn.iotstar.mapper.ProductMapper;
import vn.iotstar.repository.ProductImageRepository;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.services.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductImageRepository imageRepository;
    private final Path uploadDir = Paths.get("uploads/products");

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, ProductImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.imageRepository = imageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> products;
        if (keyword == null || keyword.isBlank()) {
            products = productRepository.findAll(pageable);
        } else {
            products = productRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
        }
        return products.map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + id));
        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO create(ProductDTO dto) {
        try {
            Product product = productMapper.toEntity(dto);
            Product saved = productRepository.save(product);
            saveImages(saved, dto.getImageFiles());
            Product result = productRepository.save(saved);
            return productMapper.toDTO(result);
        } catch (IOException e) {
            throw new RuntimeException("Không thể upload hình ảnh", e);
        }
    }

    @Override
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + id));
            productMapper.updateEntity(dto, product);
            saveImages(product, dto.getImageFiles());
            Product updated = productRepository.save(product);
            return productMapper.toDTO(updated);
        } catch (IOException e) {
            throw new RuntimeException("Không thể upload hình ảnh", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllEntities() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findEntities(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (keyword != null && !keyword.trim().isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
        }
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findEntityById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Product saveEntity(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteEntity(Product product) {
        if (product.getImages() != null) {
            for (ProductImage image : product.getImages()) {
                deleteFile(image.getImageUrl());
            }
        }
        productRepository.delete(product);
    }

    @Override
    public String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        Files.createDirectories(uploadDir);
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;
        Path target = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    @Override
    public void saveImages(Product product, List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            return;
        }
        int currentOrder = product.getImages().size();
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            String fileName = saveImage(file);
            boolean isPrimary = product.getImages().isEmpty();
            ProductImage image = ProductImage.builder()
                    .product(product)
                    .imageUrl(fileName)
                    .primary(isPrimary)
                    .displayOrder(currentOrder++)
                    .createdAt(LocalDateTime.now())
                    .build();
            product.getImages().add(image);
        }
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + id));
        deleteEntity(product);
    }

    @Override
    public Long deleteImage(Long imageId) {
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hình ảnh: " + imageId));
        Product product = image.getProduct();
        Long productId = product.getId();
        boolean wasPrimary = Boolean.TRUE.equals(image.getPrimary());

        deleteFile(image.getImageUrl());
        product.getImages().remove(image);
        imageRepository.delete(image);

        if (wasPrimary && !product.getImages().isEmpty()) {
            ProductImage newPrimary = product.getImages().get(0);
            newPrimary.setPrimary(true);
            newPrimary.setDisplayOrder(0);
        }
        return productId;
    }

    @Override
    public void deleteFile(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return;
        }
        try {
            Path uploadRoot = uploadDir.toAbsolutePath().normalize();
            Path file = uploadRoot.resolve(fileName).normalize();
            if (!file.startsWith(uploadRoot)) {
                throw new SecurityException("Tên file không hợp lệ");
            }
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.err.println("Không thể xóa file: " + fileName);
        }
    }
}
