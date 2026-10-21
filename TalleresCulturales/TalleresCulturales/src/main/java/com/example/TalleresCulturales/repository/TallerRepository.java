package com.example.TalleresCulturales.repository;

import com.example.TalleresCulturales.model.Taller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TallerRepository extends JpaRepository<Taller, Long> {

    boolean existsByTituloIgnoreCase(String titulo);

    boolean existsByTituloIgnoreCaseAndIdNot(String titulo, Long id);

    boolean existsByOrganizadorId(Long organizadorId);
}
