package ru.practicum.ewm.compilation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation, Set<EventShortDto> events) {
        return new CompilationDto(
                events,
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

    public static Compilation toCompilation(NewCompilationDto dto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(dto.pinned());
        compilation.setTitle(dto.title());
        return compilation;
    }

    public static Compilation updateCompilation(Compilation compilation, UpdateCompilationRequest request) {
        return new Compilation(
                compilation.getId(),
                request.pinned() == null ? compilation.getPinned() : request.pinned(),
                request.title() == null ? compilation.getTitle() : request.title(),
                compilation.getEvents()
        );
    }
}
