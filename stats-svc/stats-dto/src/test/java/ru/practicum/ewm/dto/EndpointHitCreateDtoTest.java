package ru.practicum.ewm.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EndpointHitCreateDtoTest {
    private static final String CORRECT_APP = "ewm-main-service";
    private static final String CORRECT_URI = "/events/1";
    private static final String CORRECT_IP = "192.163.0.1";
    private static final LocalDateTime CORRECT_TIMESTAMP = LocalDateTime.of(2022,9,6,11,0,23);
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void allCorrect() {
        final EndpointHitCreateDto endpointHitCreateDto = new EndpointHitCreateDto(CORRECT_APP, CORRECT_URI, CORRECT_IP, CORRECT_TIMESTAMP);
        final Set<ConstraintViolation<EndpointHitCreateDto>> violations = validator.validate(endpointHitCreateDto);

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void notCorrectAppShouldBeBlank () {
        final EndpointHitCreateDto endpointHitCreateDto = new EndpointHitCreateDto(" ", CORRECT_URI, CORRECT_IP, CORRECT_TIMESTAMP);
        final Set<ConstraintViolation<EndpointHitCreateDto>> violations = validator.validate(endpointHitCreateDto);

        assertThat(violations).extracting("propertyPath").extracting(Object::toString).containsOnly("app");
    }

    @Test
    public void notCorrectUriShouldBeBlank () {
        final EndpointHitCreateDto endpointHitCreateDto = new EndpointHitCreateDto(CORRECT_APP, " ", CORRECT_IP, CORRECT_TIMESTAMP);
        final Set<ConstraintViolation<EndpointHitCreateDto>> violations = validator.validate(endpointHitCreateDto);

        assertThat(violations).extracting("propertyPath").extracting(Object::toString).containsOnly("uri");
    }

    @Test
    public void notCorrectIpShouldBeBlank () {
        final EndpointHitCreateDto endpointHitCreateDto = new EndpointHitCreateDto(CORRECT_APP, CORRECT_URI, " ", CORRECT_TIMESTAMP);
        final Set<ConstraintViolation<EndpointHitCreateDto>> violations = validator.validate(endpointHitCreateDto);

        assertThat(violations).extracting("propertyPath").extracting(Object::toString).containsOnly("ip");
    }

    @Test
    public void notCorrectIpShouldBeNotIp () {
        final EndpointHitCreateDto endpointHitCreateDto = new EndpointHitCreateDto(CORRECT_APP, CORRECT_URI, "192.168.1.500", CORRECT_TIMESTAMP);
        final Set<ConstraintViolation<EndpointHitCreateDto>> violations = validator.validate(endpointHitCreateDto);

        assertThat(violations).extracting("propertyPath").extracting(Object::toString).containsOnly("ip");
    }

    @Test
    public void notCorrectTimestampShouldBeNull () {
        final EndpointHitCreateDto endpointHitCreateDto = new EndpointHitCreateDto(CORRECT_APP, CORRECT_URI, CORRECT_IP, null);
        final Set<ConstraintViolation<EndpointHitCreateDto>> violations = validator.validate(endpointHitCreateDto);

        assertThat(violations).extracting("propertyPath").extracting(Object::toString).containsOnly("timestamp");
    }

    @Test
    public void notCorrectTimestampShouldBeFuture () {
        final EndpointHitCreateDto endpointHitCreateDto = new EndpointHitCreateDto(CORRECT_APP, CORRECT_URI, CORRECT_IP, LocalDateTime.now().plusDays(1));
        final Set<ConstraintViolation<EndpointHitCreateDto>> violations = validator.validate(endpointHitCreateDto);

        assertThat(violations).extracting("propertyPath").extracting(Object::toString).containsOnly("timestamp");
    }
}