package com.customizationaudit.customization.application;

import com.customizationaudit.customization.domain.Customization;

public interface RegisterCustomizationUseCase {

    Customization register(RegisterCustomizationCommand command);
}
