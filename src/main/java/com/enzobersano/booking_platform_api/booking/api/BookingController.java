package com.enzobersano.booking_platform_api.booking.api;

import com.enzobersano.booking_platform_api.booking.api.dto.CreateBookingRequest;
import com.enzobersano.booking_platform_api.booking.api.dto.ListBookingsRequest;
import com.enzobersano.booking_platform_api.booking.api.mapper.BookingErrorMapper;
import com.enzobersano.booking_platform_api.booking.api.mapper.BookingResponseMapper;
import com.enzobersano.booking_platform_api.booking.application.CancelBookingUseCase;
import com.enzobersano.booking_platform_api.booking.application.CreateBookingUseCase;
import com.enzobersano.booking_platform_api.booking.application.GetBookingByIdUseCase;
import com.enzobersano.booking_platform_api.booking.application.ListBookingsUseCase;
import com.enzobersano.booking_platform_api.booking.application.command.CancelBookingCommand;
import com.enzobersano.booking_platform_api.booking.application.command.CreateBookingCommand;
import com.enzobersano.booking_platform_api.booking.application.query.BookingSortBy;
import com.enzobersano.booking_platform_api.booking.application.query.BookingSortDirection;
import com.enzobersano.booking_platform_api.booking.application.query.ListBookingsQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "Booking management")
public class BookingController {

    private final CreateBookingUseCase createUseCase;
    private final GetBookingByIdUseCase getByIdUseCase;
    private final ListBookingsUseCase listUseCase;
    private final CancelBookingUseCase cancelUseCase;
    private final BookingResponseMapper responseMapper;
    private final BookingErrorMapper errorMapper;

    public BookingController(
            CreateBookingUseCase createUseCase,
            GetBookingByIdUseCase getByIdUseCase,
            ListBookingsUseCase listUseCase,
            CancelBookingUseCase cancelUseCase,
            BookingResponseMapper responseMapper,
            BookingErrorMapper errorMapper
    ) {
        this.createUseCase = createUseCase;
        this.getByIdUseCase = getByIdUseCase;
        this.listUseCase = listUseCase;
        this.cancelUseCase = cancelUseCase;
        this.responseMapper = responseMapper;
        this.errorMapper = errorMapper;
    }

    // -------------------------------------------------------------------------
    // POST /api/bookings
    // -------------------------------------------------------------------------

    @Operation(summary = "Create booking")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Booking created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Conflict")
    })
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateBookingRequest request) {

        var command = new CreateBookingCommand(
                request.resourceId(),
                request.startTime(),
                request.endTime()
        );

        var result = createUseCase.execute(command);

        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(responseMapper.toResponse(result.value()));
        }

        return errorMapper.toResponse(result.error());
    }

    // -------------------------------------------------------------------------
    // GET /api/bookings/{id}
    // -------------------------------------------------------------------------

    @Operation(summary = "Get booking by id")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {

        var result = getByIdUseCase.execute(id);

        if (result.isSuccess()) {
            return ResponseEntity.ok(
                    responseMapper.toResponse(result.value())
            );
        }

        return errorMapper.toResponse(result.error());
    }

    // -------------------------------------------------------------------------
    // GET /api/bookings
    // -------------------------------------------------------------------------

    @Operation(summary = "List bookings")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<?> list(
            @ParameterObject @Valid ListBookingsRequest request) {

        var query = new ListBookingsQuery(
                request.pageOrDefault(),
                request.sizeOrDefault(),
                BookingSortBy.from(request.sortByOrDefault()).value(),
                BookingSortDirection.from(request.directionOrDefault()).value()
        );

        var result = listUseCase.execute(query);

        if (result.isSuccess()) {
            return ResponseEntity.ok(
                    responseMapper.toPagedResponse(result.value())
            );
        }

        return errorMapper.toResponse(result.error());
    }

    // -------------------------------------------------------------------------
    // PATCH /api/bookings/{id}/cancel
    // -------------------------------------------------------------------------

    @Operation(summary = "Cancel booking")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable UUID id) {

        var result = cancelUseCase.execute(
                new CancelBookingCommand(id)
        );

        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }

        return errorMapper.toResponse(result.error());
    }
}