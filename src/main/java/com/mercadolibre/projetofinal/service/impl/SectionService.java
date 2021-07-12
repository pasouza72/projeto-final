package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.model.Section;
import com.mercadolibre.projetofinal.repository.SectionRepository;
import com.mercadolibre.projetofinal.service.ISectionService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SectionService implements ISectionService {


    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Optional<Section> findById(String sectionCode) {
        return sectionRepository.findById(sectionCode);
    }

    @Override
    public Section findByCategory(String name) {
        return sectionRepository.findByName(name);
    }
}
