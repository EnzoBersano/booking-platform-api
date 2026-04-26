package com.enzobersano.booking_platform_api.resource.api;

import com.enzobersano.booking_platform_api.resource.api.dto.CreateResourceRequest;
import com.enzobersano.booking_platform_api.resource.api.mapper.ResourceErrorMapper;
import com.enzobersano.booking_platform_api.resource.api.mapper.ResourceResponseMapper;
import com.enzobersano.booking_platform_api.resource.application.*;
import com.enzobersano.booking_platform_api.resource.application.command.CreateResourceCommand;
import com.enzobersano.booking_platform_api.resource.application.command.DisableResourceCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
@Tag(name = "Resources", description = "Resource management")
public class ResourceController {

    private final CreateResourceUseCase createUseCase;
    private final GetResourceByIdUseCase getByIdUseCase;
    private final ListResourcesUseCase listUseCase;
    private final DisableResourceUseCase disableUseCase;
    private final ResourceErrorMapper errorMapper;
    private final ResourceResponseMapper responseMapper;

    public ResourceController(CreateResourceUseCase createUseCase,
                              GetResourceByIdUseCase getByIdUseCase,
                              ListResourcesUseCase listUseCase,
                              DisableResourceUseCase disableUseCase,
                              ResourceErrorMapper errorMapper,
                              ResourceResponseMapper responseMapper) {
        this.createUseCase = createUseCase;
        this.getByIdUseCase = getByIdUseCase;
        this.listUseCase = listUseCase;
        this.disableUseCase = disableUseCase;
        this.errorMapper = errorMapper;
        this.responseMapper = responseMapper;
    }


    // -------------------------------------------------------------------------
    // POST /api/resources
    // -------------------------------------------------------------------------

    @Operation(summary = "Create a new resource")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateResourceRequest request) {


        var command = new CreateResourceCommand(
                request.name(),
                request.type()
        );

        var result = createUseCase.execute(command);

        if (result.isSuccess()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(responseMapper.toResponse(result.value()));
        }

        return errorMapper.toResponse(result.error());
    }

    // -------------------------------------------------------------------------
    // GET /api/resources/{id}
    // -------------------------------------------------------------------------

    @Operation(summary = "Get resource by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resource found"),
            @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {

        var result = getByIdUseCase.execute(id);

        if (result.isSuccess()) {
            return ResponseEntity.ok(responseMapper.toResponse(result.value()));
        }

        return errorMapper.toResponse(result.error());
    }

    // -------------------------------------------------------------------------
    // GET /api/resources
    // -------------------------------------------------------------------------

    @Operation(summary = "List all resources")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of resources")
    })
    @GetMapping
    public ResponseEntity<?> list(
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size
    ) {

        var result = listUseCase.execute(page, size);


        if (result.isSuccess()) {
            return ResponseEntity.ok(
                    responseMapper.toPagedResponse(result.value())
            );
        }

        return errorMapper.toResponse(result.error());
    }

    // -------------------------------------------------------------------------
    // PATCH /api/resources/{id}/disable
    // -------------------------------------------------------------------------

    @Operation(summary = "Disable a resource")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Resource disabled"),
            @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    @PatchMapping("/{id}/disable")
    public ResponseEntity<?> disable(@PathVariable UUID id) {


        var result = disableUseCase.execute(new DisableResourceCommand(id));

        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }

        return errorMapper.toResponse(result.error());
    }
}