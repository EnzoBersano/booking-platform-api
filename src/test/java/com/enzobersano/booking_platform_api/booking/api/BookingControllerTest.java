package com.enzobersano.booking_platform_api.booking.api;

import com.enzobersano.booking_platform_api.auth.infrastructure.security.JwtAuthenticationFilter;
import com.enzobersano.booking_platform_api.auth.infrastructure.security.SecurityConfig;
import com.enzobersano.booking_platform_api.booking.api.mapper.BookingErrorMapper;
import com.enzobersano.booking_platform_api.booking.api.mapper.BookingResponseMapper;
import com.enzobersano.booking_platform_api.booking.application.*;
import com.enzobersano.booking_platform_api.booking.application.query.BookingSortBy;
import com.enzobersano.booking_platform_api.booking.application.query.BookingSortDirection;
import com.enzobersano.booking_platform_api.booking.application.query.ListBookingsQuery;
import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.booking.domain.model.BookingStatus;
import com.enzobersano.booking_platform_api.booking.domain.valueobject.TimeRange;
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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = BookingController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@ImportAutoConfiguration(exclude = SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({BookingResponseMapper.class, BookingErrorMapper.class})
@WithMockUser
class BookingControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean CreateBookingUseCase createUseCase;
    @MockBean GetBookingByIdUseCase getByIdUseCase;
    @MockBean ListBookingsUseCase listUseCase;
    @MockBean CancelBookingUseCase cancelUseCase;

    @MockBean JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean UserDetailsService userDetailsService;

    private static final String BASE_URL = "/api/bookings";

    private String json(Object body) throws Exception {
        return objectMapper.writeValueAsString(body);
    }

    record CreateBody(UUID resourceId, Instant startTime, Instant endTime) {}

    // =========================================================================
    // POST /api/bookings
    // =========================================================================

    @Nested
    @DisplayName("POST /api/bookings")
    class Create {

        @Test
        @DisplayName("201 — booking created successfully")
        void returns201OnSuccess() throws Exception {

            var booking = Booking.reconstitute(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    new TimeRange(
                            Instant.now().plusSeconds(3600),
                            Instant.now().plusSeconds(7200)
                    ),
                    BookingStatus.ACTIVE
            );

            when(createUseCase.execute(any()))
                    .thenReturn(Result.success(booking));

            mockMvc.perform(post(BASE_URL).with(csrf())
                            .contentType(APPLICATION_JSON)
                            .content(json(new CreateBody(
                                    UUID.randomUUID(),
                                    Instant.now().plusSeconds(3600),
                                    Instant.now().plusSeconds(7200)
                            ))))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("400 — invalid time range rejected")
        void returns400OnInvalidTimeRange() throws Exception {

            when(createUseCase.execute(any()))
                    .thenReturn(Result.failure(
                            new BookingFailure.InvalidTimeRange("Invalid time")
                    ));

            mockMvc.perform(post(BASE_URL).with(csrf())
                            .contentType(APPLICATION_JSON)
                            .content(json(new CreateBody(
                                    UUID.randomUUID(),
                                    Instant.now(),
                                    Instant.now().minusSeconds(3600)
                            ))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message")
                            .value("Invalid booking time range: Invalid time"));
        }
    }

    // =========================================================================
    // GET /api/bookings/{id}
    // =========================================================================

    @Nested
    @DisplayName("GET /api/bookings/{id}")
    class GetById {

        @Test
        @DisplayName("200 — booking found")
        void returns200() throws Exception {

            var booking = Booking.reconstitute(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    new TimeRange(
                            Instant.now().plusSeconds(3600),
                            Instant.now().plusSeconds(7200)
                    ),
                    BookingStatus.ACTIVE
            );

            when(getByIdUseCase.execute(any()))
                    .thenReturn(Result.success(booking));

            mockMvc.perform(get(BASE_URL + "/{id}", UUID.randomUUID()))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("404 — booking not found")
        void returns404() throws Exception {

            when(getByIdUseCase.execute(any()))
                    .thenReturn(Result.failure(new BookingFailure.NotFound()));

            mockMvc.perform(get(BASE_URL + "/{id}", UUID.randomUUID()))
                    .andExpect(status().isNotFound());
        }
    }

    // =========================================================================
    // GET /api/bookings
    // =========================================================================

    @Nested
    @DisplayName("GET /api/bookings")
    class Lists {

        @Test
        @DisplayName("200 — valid pagination request")
        void returns200OnSuccess() throws Exception {

            var query = new ListBookingsQuery(
                    0,
                    10,
                    BookingSortBy.START_TIME,
                    BookingSortDirection.ASC
            );

            when(listUseCase.execute(any()))
                    .thenReturn(Result.success(
                            new PageResult<Booking>(
                                    List.of(),
                                    0,
                                    10,
                                    0,
                                    0,
                                    true,
                                    true
                            )
                    ));

            mockMvc.perform(get(BASE_URL)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sortBy", "startTime")
                            .param("direction", "asc"))
                    .andExpect(status().isOk());

            verify(listUseCase).execute(any(ListBookingsQuery.class));
        }
    }

    // =========================================================================
    // PATCH /api/bookings/{id}/cancel
    // =========================================================================

    @Nested
    @DisplayName("PATCH /api/bookings/{id}/cancel")
    class Cancel {

        @Test
        @DisplayName("204 — booking cancelled")
        void returns204() throws Exception {

            when(cancelUseCase.execute(any()))
                    .thenReturn(Result.success(null));

            mockMvc.perform(patch(BASE_URL + "/{id}/cancel", UUID.randomUUID())
                            .with(csrf()))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("404 — booking not found")
        void returns404() throws Exception {

            when(cancelUseCase.execute(any()))
                    .thenReturn(Result.failure(new BookingFailure.NotFound()));

            mockMvc.perform(patch(BASE_URL + "/{id}/cancel", UUID.randomUUID())
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }
    }
}