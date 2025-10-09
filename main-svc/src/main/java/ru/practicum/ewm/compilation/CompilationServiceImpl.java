package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.helper.StatsHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final StatsHelper statsHelper;

    // Public access
    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));
        Page<Compilation> compilations = pinned == null ?
                compilationRepository.findAll(pageable) : compilationRepository.findByPinned(pinned, pageable);
        return statsHelper.toManyCompilationDto(compilations.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long compilationId) {
        return statsHelper.toCompilationDto(findCompilationById(compilationId));
    }

    // Admin access
    @Override
    public CompilationDto createCompilationForAdmin(NewCompilationDto dto) {
        Compilation compilation = CompilationMapper.toCompilation(dto);
        patchCompilationEvents(compilation, dto.events());
        Compilation createCompilation = compilationRepository.save(compilation);
        log.info("Create compilation: {}", createCompilation);
        return statsHelper.toCompilationDto(createCompilation);
    }

    @Override
    public void deleteCompilationByIdForAdmin(Long compilationId) {
        findCompilationById(compilationId);
        compilationRepository.deleteById(compilationId);
        log.info("Delete compilation: id = {}", compilationId);
    }

    @Override
    public CompilationDto updateCompilationByIdForAdmin(Long compilationId, UpdateCompilationRequest request) {
        Compilation compilation = CompilationMapper.updateCompilation(findCompilationById(compilationId), request);
        patchCompilationEvents(compilation, request.events());
        Compilation updateCompilation = compilationRepository.save(compilation);
        log.info("Update compilation: {}", updateCompilation);
        return statsHelper.toCompilationDto(updateCompilation);
    }

    // Additional
    private Compilation findCompilationById(Long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(
                () -> new NotFoundException(String.format("Compilation not found: id = %d", compilationId)));
    }

    private void patchCompilationEvents(Compilation compilation, Set<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            List<Event> events = eventRepository.findAllById(ids);
            if (ids.size() != events.size()) {
                throw new NotFoundException("Not all events in the compilation were found.");
            }
            compilation.setEvents(new HashSet<>(events));
        }
    }
}
