package vn.iotstar.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDTO {
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 200, message = "Tên sản phẩm tối đa 200 ký tự")
    private String name;

    private String brand;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0", message = "Giá phải >= 0")
    private BigDecimal price;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải >= 0")
    private Integer quantity;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    private String description;

    // Existing saved images
    private List<ProductImageDTO> images = new ArrayList<>();

    // Uploaded multi-part files
    private List<MultipartFile> imageFiles = new ArrayList<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String brand, BigDecimal price, Integer quantity, String description, List<ProductImageDTO> images, List<MultipartFile> imageFiles) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.images = images != null ? images : new ArrayList<>();
        this.imageFiles = imageFiles != null ? imageFiles : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<ProductImageDTO> getImages() { return images; }
    public void setImages(List<ProductImageDTO> images) { this.images = images; }

    public List<MultipartFile> getImageFiles() { return imageFiles; }
    public void setImageFiles(List<MultipartFile> imageFiles) { this.imageFiles = imageFiles; }

    public static ProductDTOBuilder builder() {
        return new ProductDTOBuilder();
    }

    public static class ProductDTOBuilder {
        private Long id;
        private String name;
        private String brand;
        private BigDecimal price;
        private Integer quantity;
        private String description;
        private List<ProductImageDTO> images = new ArrayList<>();
        private List<MultipartFile> imageFiles = new ArrayList<>();

        public ProductDTOBuilder id(Long id) { this.id = id; return this; }
        public ProductDTOBuilder name(String name) { this.name = name; return this; }
        public ProductDTOBuilder brand(String brand) { this.brand = brand; return this; }
        public ProductDTOBuilder price(BigDecimal price) { this.price = price; return this; }
        public ProductDTOBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public ProductDTOBuilder description(String description) { this.description = description; return this; }
        public ProductDTOBuilder images(List<ProductImageDTO> images) { this.images = images; return this; }
        public ProductDTOBuilder imageFiles(List<MultipartFile> imageFiles) { this.imageFiles = imageFiles; return this; }

        public ProductDTO build() {
            return new ProductDTO(id, name, brand, price, quantity, description, images, imageFiles);
        }
    }
}
