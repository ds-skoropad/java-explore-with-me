package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compilationId);

    CompilationDto createCompilationForAdmin(NewCompilationDto newCompilationDto);

    void deleteCompilationByIdForAdmin(Long compilationId);

    CompilationDto updateCompilationByIdForAdmin(Long compilationId, UpdateCompilationRequest request);
}
