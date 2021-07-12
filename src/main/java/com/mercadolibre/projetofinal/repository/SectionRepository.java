package com.mercadolibre.projetofinal.repository;

import com.mercadolibre.projetofinal.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, String> {
    Section findByName(String name);
}
