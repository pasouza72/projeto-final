package com.mercadolibre.projetofinal.service;

import com.mercadolibre.projetofinal.model.Section;

import java.util.Optional;

public interface ISectionService {
    Optional<Section> findById(String sectionCode);
    Section findByCategory(String name);
}
