package com.customizationaudit.customization.application;

import com.customizationaudit.customization.domain.Customization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomizationRepository {

    Customization save(Customization customization);

    List<Customization> findAll();

    Optional<Customization> findById(UUID id);
}
