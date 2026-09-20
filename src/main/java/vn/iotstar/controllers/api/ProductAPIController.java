package vn.iotstar.controllers.api;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.ProductImage;
import vn.iotstar.model.Response;
import vn.iotstar.services.CategoryService;
import vn.iotstar.services.ProductService;
import vn.iotstar.services.StorageService;

@RestController
@RequestMapping("/api/product")
public class ProductAPIController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StorageService storageService;

    @GetMapping
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<>(new Response(true, "Thành công", productService.findAllEntities()), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<?> getPageProduct(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Product> productPage = productService.findEntities(keyword, page, size);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        Optional<Product> optProduct = productService.findEntityById(id);
        if (optProduct.isPresent()) {
            return new ResponseEntity<>(new Response(true, "Thành công", optProduct.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy sản phẩm", null), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(
            @RequestParam("productName") String productName,
            @RequestParam(value = "brand", required = false, defaultValue = "") String brand,
            @RequestParam("unitPrice") Double unitPrice,
            @RequestParam("quantity") Integer quantity,
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Optional<Product> optProduct = productService.findByName(productName);
        if (optProduct.isPresent()) {
            return new ResponseEntity<>(new Response(false, "Sản phẩm này đã tồn tại trong hệ thống", null), HttpStatus.BAD_REQUEST);
        }

        Product product = new Product();
        product.setName(productName);
        product.setBrand(brand);
        product.setPrice(BigDecimal.valueOf(unitPrice));
        product.setQuantity(quantity);
        product.setDescription(description);

        if (categoryId != null) {
            Optional<Category> optCate = categoryService.findById(categoryId);
            optCate.ifPresent(product::setCategory);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = storageService.store(imageFile, "products");
            if (filename != null) {
                ProductImage img = ProductImage.builder()
                        .product(product)
                        .imageUrl(filename)
                        .primary(true)
                        .displayOrder(0)
                        .build();
                product.getImages().add(img);
            }
        }

        Product saved = productService.saveEntity(product);
        return new ResponseEntity<>(new Response(true, "Thêm sản phẩm thành công", saved), HttpStatus.OK);
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(
            @RequestParam("id") Long id,
            @RequestParam("productName") String productName,
            @RequestParam(value = "brand", required = false, defaultValue = "") String brand,
            @RequestParam("unitPrice") Double unitPrice,
            @RequestParam("quantity") Integer quantity,
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Optional<Product> optProduct = productService.findEntityById(id);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy sản phẩm", null), HttpStatus.BAD_REQUEST);
        }

        Product product = optProduct.get();
        product.setName(productName);
        product.setBrand(brand);
        product.setPrice(BigDecimal.valueOf(unitPrice));
        product.setQuantity(quantity);
        product.setDescription(description);

        if (categoryId != null) {
            Optional<Category> optCate = categoryService.findById(categoryId);
            optCate.ifPresent(product::setCategory);
        } else {
            product.setCategory(null);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = storageService.store(imageFile, "products");
            if (filename != null) {
                if (!product.getImages().isEmpty()) {
                    product.getImages().get(0).setImageUrl(filename);
                } else {
                    ProductImage img = ProductImage.builder()
                            .product(product)
                            .imageUrl(filename)
                            .primary(true)
                            .displayOrder(0)
                            .build();
                    product.getImages().add(img);
                }
            }
        }

        Product updated = productService.saveEntity(product);
        return new ResponseEntity<>(new Response(true, "Cập nhật sản phẩm thành công", updated), HttpStatus.OK);
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam("id") Long id) {
        Optional<Product> optProduct = productService.findEntityById(id);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy sản phẩm", null), HttpStatus.BAD_REQUEST);
        }
        productService.deleteEntity(optProduct.get());
        return ResponseEntity.ok(new Response(true, "Xóa sản phẩm thành công", optProduct.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductPath(@PathVariable("id") Long id) {
        Optional<Product> optProduct = productService.findEntityById(id);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy sản phẩm", null), HttpStatus.BAD_REQUEST);
        }
        productService.deleteEntity(optProduct.get());
        return ResponseEntity.ok(new Response(true, "Xóa sản phẩm thành công", optProduct.get()));
    }
}
