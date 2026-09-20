package vn.iotstar.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import vn.iotstar.entity.Category;

public interface CategoryService {
    List<Category> findAll();
    Page<Category> findAll(String keyword, int page, int size);
    Optional<Category> findById(Long id);
    Optional<Category> findByCategoryName(String categoryName);
    Category save(Category category);
    void delete(Category category);
    void deleteById(Long id);
}
