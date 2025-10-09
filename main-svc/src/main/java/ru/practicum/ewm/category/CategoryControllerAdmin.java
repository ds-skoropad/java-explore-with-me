package ru.practicum.ewm.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.dto.UpdateCategoryDto;

@Validated
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryControllerAdmin {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.createCategory(newCategoryDto);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable @Min(1) Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDto updateCategory(
            @PathVariable @Min(1) Long categoryId,
            @RequestBody @Valid UpdateCategoryDto updateCategoryDto) {
        return categoryService.updateCategory(new UpdateCategoryDto(categoryId, updateCategoryDto.name()));
    }
}
