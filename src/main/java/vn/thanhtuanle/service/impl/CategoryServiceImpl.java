package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.entity.Category;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.CategoryDTO;
import vn.thanhtuanle.repository.CategoryRepository;
import vn.thanhtuanle.service.CategoryService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<CategoryDTO> findAll(Pageable pageable, String query, Boolean delFlag) {
        Specification<Category> spec = Specification.where(null);

        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> {
                String searchPattern = "%" + query.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern)
                );
            });
        }

        if (delFlag != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("delFlag"), delFlag));
        }

        Page<Category> categories = categoryRepository.findAll(spec, pageable);
        return categories.map(r -> modelMapper.map(r, CategoryDTO.class));
    }

    @Transactional
    @Override
    public CategoryDTO createCategory(CategoryDTO req) {
        Category category = modelMapper.map(req, Category.class);

        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
    }

    @Override
    public CategoryDTO getCategoryById(Integer id) {
        Category Category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        return modelMapper.map(Category, CategoryDTO.class);
    }

    @Transactional
    @Override
    public CategoryDTO updateCategory(Integer id, CategoryDTO req) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        Boolean oldDelFlag = category.getDelFlag();
        modelMapper.map(req, category);

        category.setId(id);

        if (req.getDelFlag() != null) {
            category.setDelFlag(req.getDelFlag());
        } else {
            category.setDelFlag(oldDelFlag);
        }

        categoryRepository.saveAndFlush(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        category.setDelFlag(true);

        categoryRepository.save(category);
    }

    @Transactional
    @Override
    public CategoryDTO updateCategoryStatus(Integer id, Boolean isActive) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setDelFlag(isActive);
        categoryRepository.save(category);

        return modelMapper.map(category, CategoryDTO.class);
    }
}
