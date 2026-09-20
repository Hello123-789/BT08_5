package vn.iotstar.dto;

public class ProductImageDTO {
    private Long id;
    private String imageUrl;
    private Boolean primary;
    private Integer displayOrder;

    public ProductImageDTO() {
    }

    public ProductImageDTO(Long id, String imageUrl, Boolean primary, Integer displayOrder) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.primary = primary;
        this.displayOrder = displayOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getPrimary() { return primary; }
    public void setPrimary(Boolean primary) { this.primary = primary; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public static ProductImageDTOBuilder builder() {
        return new ProductImageDTOBuilder();
    }

    public static class ProductImageDTOBuilder {
        private Long id;
        private String imageUrl;
        private Boolean primary;
        private Integer displayOrder;

        public ProductImageDTOBuilder id(Long id) { this.id = id; return this; }
        public ProductImageDTOBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ProductImageDTOBuilder primary(Boolean primary) { this.primary = primary; return this; }
        public ProductImageDTOBuilder displayOrder(Integer displayOrder) { this.displayOrder = displayOrder; return this; }

        public ProductImageDTO build() {
            return new ProductImageDTO(id, imageUrl, primary, displayOrder);
        }
    }
}
