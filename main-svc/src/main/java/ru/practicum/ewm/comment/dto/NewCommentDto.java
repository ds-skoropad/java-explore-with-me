package ru.practicum.ewm.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewCommentDto(
        @NotBlank @Size(min = 1, max = 7000)
        String text
) {
}
