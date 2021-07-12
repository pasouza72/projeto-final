package com.mercadolibre.projetofinal.unit.service;

import com.mercadolibre.projetofinal.model.Section;
import com.mercadolibre.projetofinal.repository.SectionRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mercadolibre.projetofinal.service.impl.SectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

/* SectionService just use repository to save data from SectionController */
/* tests will be done just for coverage, cause repository is mocked */
@ExtendWith(MockitoExtension.class)
public class SectionServiceTest {

    @Mock
    private SectionRepository sectionRepository;
    @InjectMocks
    private SectionService sectionService;

    @Test
    void shouldReturnSectionById() {
        Section sectionToFind = new Section("section1", "Section Teste 1", Byte.valueOf("4"), Byte.valueOf("15"), new HashSet<>(), null);;
        when(sectionRepository.findById(any())).thenReturn(java.util.Optional.of(sectionToFind));
        Section sectionFound = sectionService.findById(sectionToFind.getId()).orElse(null);
        assertEquals(sectionToFind.getId(), sectionFound.getId());
    }

    @Test
    void shouldReturnSectionByCategory() {
        Section sectionToFind = new Section("section1", "Section Teste 1", Byte.valueOf("4"), Byte.valueOf("15"), new HashSet<>(), null);;
        when(sectionRepository.findByName(any())).thenReturn(sectionToFind);
        Section sectionFound = sectionService.findByCategory(sectionToFind.getName());
        assertEquals(sectionToFind.getName(), sectionFound.getName());
    }

    }
