package com.enzobersano.booking_platform_api.auth.application.port;

import java.util.UUID;

public interface CurrentUserPort {
    UUID getCurrentUserId();
}
