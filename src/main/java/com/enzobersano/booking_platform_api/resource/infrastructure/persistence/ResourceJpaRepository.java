package com.enzobersano.booking_platform_api.resource.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface ResourceJpaRepository extends JpaRepository<ResourceJpaEntity, UUID> {
}