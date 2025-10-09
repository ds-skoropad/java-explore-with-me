package ru.practicum.ewm.compilation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationControllerAdmin {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilationForAdmin(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("POST createCompilationForAdmin: {}", newCompilationDto);
        return compilationService.createCompilationForAdmin(newCompilationDto);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationByIdForAdmin(@PathVariable @Min(1) Long compilationId) {
        log.info("DELETE deleteCompilationByIdForAdmin: compilationId={}", compilationId);
        compilationService.deleteCompilationByIdForAdmin(compilationId);
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto updateCompilationByIdForAdmin(
            @PathVariable @Min(1) Long compilationId,
            @RequestBody @Valid UpdateCompilationRequest request) {
        log.info("PATCH updateCompilationByIdForAdmin: compilationId={}, {}", compilationId, request);
        return compilationService.updateCompilationByIdForAdmin(compilationId, request);
    }
}
