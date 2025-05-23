package vn.thanhtuanle.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.thanhtuanle.model.dto.CategoryDTO;

public interface CategoryService {
    Page<CategoryDTO> findAll(Pageable pageable, String query, Boolean delFlag);

    @Transactional
    CategoryDTO createCategory(CategoryDTO req);

    CategoryDTO getCategoryById(Integer id);

    @Transactional
    CategoryDTO updateCategory(Integer id, CategoryDTO req);

    void deleteCategory(Integer id);

    @Transactional
    CategoryDTO updateCategoryStatus(Integer id, Boolean isActive);
}
