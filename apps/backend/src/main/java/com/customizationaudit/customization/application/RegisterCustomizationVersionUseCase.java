package com.customizationaudit.customization.application;

import com.customizationaudit.customization.domain.CustomizationVersion;

public interface RegisterCustomizationVersionUseCase {

    CustomizationVersion register(RegisterCustomizationVersionCommand command);
}
