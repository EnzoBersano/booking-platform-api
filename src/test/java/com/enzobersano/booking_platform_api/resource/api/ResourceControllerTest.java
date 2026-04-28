package com.enzobersano.booking_platform_api.resource.api;

import com.enzobersano.booking_platform_api.resource.api.mapper.ListResourcesRequestMapper;
import com.enzobersano.booking_platform_api.resource.api.mapper.ResourceErrorMapper;
import com.enzobersano.booking_platform_api.resource.api.mapper.ResourceResponseMapper;
import com.enzobersano.booking_platform_api.resource.application.CreateResourceUseCase;
import com.enzobersano.booking_platform_api.resource.application.DisableResourceUseCase;
import com.enzobersano.booking_platform_api.resource.application.GetResourceByIdUseCase;
import com.enzobersano.booking_platform_api.resource.application.ListResourcesUseCase;
import com.enzobersano.booking_platform_api.resource.application.query.ListResourcesQuery;
import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.auth.infrastructure.security.JwtAuthenticationFilter;
import com.enzobersano.booking_platform_api.auth.infrastructure.security.SecurityConfig;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.resource.application.query.ResourceSortBy;
import com.enzobersano.booking_platform_api.resource.application.query.ResourceSortDirection;
import com.enzobersano.booking_platform_api.resource.domain.model.ResourceType;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;
import com.enzobersano.booking_platform_api.shared.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ResourceController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@ImportAutoConfiguration(exclude = SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({ResourceErrorMapper.class, ResourceResponseMapper.class})
@WithMockUser
class ResourceControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean CreateResourceUseCase createUseCase;
    @MockBean GetResourceByIdUseCase getByIdUseCase;
    @MockBean ListResourcesUseCase listUseCase;
    @MockBean DisableResourceUseCase disableUseCase;
    @MockBean ListResourcesRequestMapper listRequestMapper;

    @MockBean JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean UserDetailsService userDetailsService;

    private static final String BASE_URL = "/api/resources";

    private String json(Object body) throws Exception {
        return objectMapper.writeValueAsString(body);
    }

    record CreateBody(String name, String type) {}

    // =========================================================================
    // POST /api/resources
    // =========================================================================

    @Nested
    @DisplayName("POST /api/resources")
    class Create {

        @Test
        @DisplayName("201 — valid payload creates resource")
        void returns201OnSuccess() throws Exception {

            var resource = Resource.create("Service 2", ResourceType.SERVICE);
            when(createUseCase.execute(any()))
                    .thenReturn(Result.success(resource));

            mockMvc.perform(post(BASE_URL).with(csrf())
                            .contentType(APPLICATION_JSON)
                            .content(json(new CreateBody("Service 2", "SERVICE"))))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("400 — blank name fails Bean Validation")
        void returns400OnBlankName() throws Exception {

            mockMvc.perform(post(BASE_URL).with(csrf())
                            .contentType(APPLICATION_JSON)
                            .content(json(new CreateBody("", "ROOM"))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 — invalid type rejected by use case")
        void returns400OnInvalidType() throws Exception {

            when(createUseCase.execute(any()))
                    .thenReturn(Result.failure(
                            new ResourceFailure.InvalidType("INVALID")
                    ));

            mockMvc.perform(post(BASE_URL).with(csrf())
                            .contentType(APPLICATION_JSON)
                            .content(json(new CreateBody("Room A", "INVALID"))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message")
                            .value("Invalid resource type: INVALID"));
        }
    }

    // =========================================================================
    // GET /api/resources/{id}
    // =========================================================================

    @Nested
    @DisplayName("GET /api/resources/{id}")
    class GetById {

        @Test
        @DisplayName("200 — existing resource")
        void returns200WhenFound() throws Exception {
            var resource = Resource.create("Flight 123", ResourceType.FLIGHT);
            when(getByIdUseCase.execute(any()))
                    .thenReturn(Result.success(resource));

            mockMvc.perform(get(BASE_URL + "/{id}", UUID.randomUUID()))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("404 — resource not found")
        void returns404WhenMissing() throws Exception {

            when(getByIdUseCase.execute(any()))
                    .thenReturn(Result.failure(
                            new ResourceFailure.NotFound()
                    ));

            mockMvc.perform(get(BASE_URL + "/{id}", UUID.randomUUID()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message")
                            .value("Resource not found"));
        }
    }

    // =========================================================================
    // GET /api/resources
    // =========================================================================

    @Nested
    @DisplayName("GET /api/resources")
    class Lists {

        @Test
        @DisplayName("200 — valid pagination request")
        void returns200OnSuccess() throws Exception {

            var query = new ListResourcesQuery(0, 10, ResourceSortBy.NAME, ResourceSortDirection.ASC);

            when(listRequestMapper.toQuery(any()))
                    .thenReturn(Result.success(query));

            var page = new PageResult<Resource>(
                    List.of(Resource.create("Room A", ResourceType.ROOM)), // content
                    0,   // page
                    10,  // size
                    1L,  // totalElements
                    1,   // totalPages
                    true, // first
                    true  // last
            );

            when(listUseCase.execute(any()))
                    .thenReturn(Result.success(page));

            mockMvc.perform(get(BASE_URL)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sortBy", "name")
                            .param("direction", "asc"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("400 — invalid sort field")
        void returns400OnInvalidSortBy() throws Exception {

            when(listRequestMapper.toQuery(any()))
                    .thenReturn(Result.failure(
                            new ResourceFailure.InvalidSortBy("price")
                    ));

            mockMvc.perform(get(BASE_URL)
                            .param("sortBy", "price"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message")
                            .value("Invalid sort field: price"));
        }

        @Test
        @DisplayName("400 — invalid sort direction")
        void returns400OnInvalidDirection() throws Exception {

            when(listRequestMapper.toQuery(any()))
                    .thenReturn(Result.failure(
                            new ResourceFailure.InvalidSortDirection("up")
                    ));

            mockMvc.perform(get(BASE_URL)
                            .param("direction", "up"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message")
                            .value("Invalid sort direction: up. Allowed: asc, desc"));
        }

        @Test
        @DisplayName("400 — negative page fails Bean Validation")
        void returns400OnNegativePage() throws Exception {

            mockMvc.perform(get(BASE_URL)
                            .param("page", "-1"))
                    .andExpect(status().isBadRequest());
        }
    }

    // =========================================================================
    // PATCH /api/resources/{id}/disable
    // =========================================================================

    @Nested
    @DisplayName("PATCH /api/resources/{id}/disable")
    class Disable {

        @Test
        @DisplayName("204 — disable successful")
        void returns204OnSuccess() throws Exception {

            when(disableUseCase.execute(any()))
                    .thenReturn(Result.success(null));

            mockMvc.perform(patch(BASE_URL + "/{id}/disable", UUID.randomUUID())
                            .with(csrf()))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("404 — resource not found")
        void returns404WhenMissing() throws Exception {

            when(disableUseCase.execute(any()))
                    .thenReturn(Result.failure(
                            new ResourceFailure.NotFound()
                    ));

            mockMvc.perform(patch(BASE_URL + "/{id}/disable", UUID.randomUUID())
                            .with(csrf()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message")
                            .value("Resource not found"));
        }
    }
}