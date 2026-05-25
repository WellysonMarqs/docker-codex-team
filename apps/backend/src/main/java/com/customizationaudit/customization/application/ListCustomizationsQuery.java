package com.customizationaudit.customization.application;

import com.customizationaudit.customization.domain.Customization;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListCustomizationsQuery {

    private final CustomizationRepository customizationRepository;

    public ListCustomizationsQuery(CustomizationRepository customizationRepository) {
        this.customizationRepository = customizationRepository;
    }

    @Transactional(readOnly = true)
    public List<Customization> list() {
        return customizationRepository.findAll();
    }
}
