package vn.iotstar.controllers.graphql;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import vn.iotstar.dto.CategoryInput;
import vn.iotstar.dto.CategoryPageDTO;
import vn.iotstar.entity.Category;
import vn.iotstar.services.CategoryService;

@Controller
public class GraphQLCategoryController {

    @Autowired
    private CategoryService categoryService;

    @QueryMapping
    public List<Category> categories() {
        return categoryService.findAll();
    }

    @QueryMapping
    public Category categoryById(@Argument Long id) {
        return categoryService.findById(id).orElse(null);
    }

    @QueryMapping
    public CategoryPageDTO categoriesPage(
            @Argument String keyword,
            @Argument Integer page,
            @Argument Integer size) {
        int pageNum = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 5;
        Page<Category> categoryPage = categoryService.findAll(keyword, pageNum, pageSize);
        return new CategoryPageDTO(categoryPage);
    }

    @MutationMapping
    public Category createCategory(@Argument CategoryInput input) {
        Category category = new Category();
        category.setCategoryName(input.getCategoryName());
        category.setIcon(input.getIcon());
        return categoryService.save(category);
    }

    @MutationMapping
    public Category updateCategory(@Argument Long id, @Argument CategoryInput input) {
        Optional<Category> optCategory = categoryService.findById(id);
        if (optCategory.isPresent()) {
            Category category = optCategory.get();
            if (input.getCategoryName() != null && !input.getCategoryName().isBlank()) {
                category.setCategoryName(input.getCategoryName());
            }
            if (input.getIcon() != null) {
                category.setIcon(input.getIcon());
            }
            return categoryService.save(category);
        }
        return null;
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        Optional<Category> optCategory = categoryService.findById(id);
        if (optCategory.isPresent()) {
            categoryService.delete(optCategory.get());
            return true;
        }
        return false;
    }
}
