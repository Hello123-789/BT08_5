package vn.iotstar.controllers.graphql;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import vn.iotstar.dto.ProductInput;
import vn.iotstar.dto.ProductPageDTO;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.ProductImage;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.services.CategoryService;
import vn.iotstar.services.ProductService;

@Controller
public class GraphQLProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @QueryMapping
    public List<Product> productsSortedByPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId);
    }

    @QueryMapping
    public List<Product> products() {
        return productService.findAllEntities();
    }

    @QueryMapping
    public Product productById(@Argument Long id) {
        return productService.findEntityById(id).orElse(null);
    }

    @QueryMapping
    public ProductPageDTO productsPage(
            @Argument String keyword,
            @Argument Integer page,
            @Argument Integer size) {
        int pageNum = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 5;
        Page<Product> productPage = productService.findEntities(keyword, pageNum, pageSize);
        return new ProductPageDTO(productPage);
    }

    @MutationMapping
    public Product createProduct(@Argument ProductInput input) {
        Product product = new Product();
        product.setName(input.getName());
        product.setBrand(input.getBrand());
        product.setPrice(BigDecimal.valueOf(input.getPrice()));
        product.setQuantity(input.getQuantity());
        product.setDescription(input.getDescription());

        if (input.getCategoryId() != null) {
            Optional<Category> optCategory = categoryService.findById(input.getCategoryId());
            optCategory.ifPresent(product::setCategory);
        }

        if (input.getImageName() != null && !input.getImageName().isBlank()) {
            ProductImage img = ProductImage.builder()
                    .product(product)
                    .imageUrl(input.getImageName())
                    .primary(true)
                    .displayOrder(0)
                    .build();
            product.getImages().add(img);
        }

        return productService.saveEntity(product);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument ProductInput input) {
        Optional<Product> optProduct = productService.findEntityById(id);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            if (input.getName() != null) product.setName(input.getName());
            if (input.getBrand() != null) product.setBrand(input.getBrand());
            if (input.getPrice() != null) product.setPrice(BigDecimal.valueOf(input.getPrice()));
            if (input.getQuantity() != null) product.setQuantity(input.getQuantity());
            if (input.getDescription() != null) product.setDescription(input.getDescription());

            if (input.getCategoryId() != null) {
                Optional<Category> optCategory = categoryService.findById(input.getCategoryId());
                if (optCategory.isPresent()) {
                    product.setCategory(optCategory.get());
                } else {
                    product.setCategory(null);
                }
            }

            if (input.getImageName() != null && !input.getImageName().isBlank()) {
                if (!product.getImages().isEmpty()) {
                    product.getImages().get(0).setImageUrl(input.getImageName());
                } else {
                    ProductImage img = ProductImage.builder()
                            .product(product)
                            .imageUrl(input.getImageName())
                            .primary(true)
                            .displayOrder(0)
                            .build();
                    product.getImages().add(img);
                }
            }

            return productService.saveEntity(product);
        }
        return null;
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        Optional<Product> optProduct = productService.findEntityById(id);
        if (optProduct.isPresent()) {
            productService.deleteEntity(optProduct.get());
            return true;
        }
        return false;
    }
}
