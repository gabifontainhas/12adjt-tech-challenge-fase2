package br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Getter
public class UserJpaEntity {

        @Id
        private UUID id;

        @Column(unique = true)
        private String email;

        private String name;

        private LocalDate lastUpdate;

        public UserJpaEntity(UUID id, String email, String name, LocalDate lastUpdate) {
                this.id = id;
                this.email = email;
                this.name = name;
                this.lastUpdate = lastUpdate;
        }

}
