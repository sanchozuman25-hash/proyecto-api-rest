package com.example.TalleresCulturales.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "participantes")
@Getter
@Setter
@NoArgsConstructor
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombreCompleto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participante otro)) return false;
        return getId() != null && getId().equals(otro.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
