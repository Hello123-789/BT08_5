package vn.iotstar.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "is_primary")
    private Boolean primary = false;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ProductImage() {
    }

    public ProductImage(Long id, Product product, String imageUrl, Boolean primary, Integer displayOrder, LocalDateTime createdAt) {
        this.id = id;
        this.product = product;
        this.imageUrl = imageUrl;
        this.primary = primary != null ? primary : false;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getPrimary() { return primary; }
    public void setPrimary(Boolean primary) { this.primary = primary; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static ProductImageBuilder builder() {
        return new ProductImageBuilder();
    }

    public static class ProductImageBuilder {
        private Long id;
        private Product product;
        private String imageUrl;
        private Boolean primary;
        private Integer displayOrder;
        private LocalDateTime createdAt;

        public ProductImageBuilder id(Long id) { this.id = id; return this; }
        public ProductImageBuilder product(Product product) { this.product = product; return this; }
        public ProductImageBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ProductImageBuilder primary(Boolean primary) { this.primary = primary; return this; }
        public ProductImageBuilder displayOrder(Integer displayOrder) { this.displayOrder = displayOrder; return this; }
        public ProductImageBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ProductImage build() {
            return new ProductImage(id, product, imageUrl, primary, displayOrder, createdAt);
        }
    }
}
