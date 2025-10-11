package ru.practicum.ewm.category;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.dto.UpdateCategoryDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMapper {

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(
                null,
                newCategoryDto.name()
        );
    }

    public static CategoryDto toCategoryDto(Category createCategory) {
        return new CategoryDto(
                createCategory.getId(),
                createCategory.getName()
        );
    }

    public static Category updateCategory(Category category, UpdateCategoryDto updateCategoryDto) {
        return new Category(
                category.getId(),
                updateCategoryDto.name() == null ? category.getName() : updateCategoryDto.name()
        );
    }
}
