package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.dto.UpdateCategoryDto;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    // Admin access
    @Override
    public CategoryDto createCategory(NewCategoryDto dto) {
        Category createCategory = categoryRepository.save(CategoryMapper.toCategory(dto));
        log.info("Create category: {}", createCategory);
        return CategoryMapper.toCategoryDto(createCategory);
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        if (eventRepository.existsByCategoryId(categoryId)) {
            throw new ConflictException("Сannot delete a category with associated events.");
        }
        categoryRepository.deleteById(categoryId);
        log.info("Delete category: id = {}", categoryId);
    }

    @Override
    public CategoryDto updateCategory(UpdateCategoryDto dto) {
        Category category = findCategoryById(dto.id());
        if (!category.getName().equals(dto.name())) {
            category = categoryRepository.save(CategoryMapper.updateCategory(category, dto));
        }
        log.info("Update category: {}", category);
        return CategoryMapper.toCategoryDto(category);
    }

    // Public access
    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));
        return categoryRepository.findAll(pageable)
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long categoryId) {
        return CategoryMapper.toCategoryDto(findCategoryById(categoryId));
    }

    // Additional
    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category not found: id = %d", categoryId)));
    }
}
