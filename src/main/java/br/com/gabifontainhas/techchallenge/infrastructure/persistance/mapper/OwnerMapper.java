package br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper;

import br.com.gabifontainhas.techchallenge.domain.entities.Owner;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity.OwnerJpaEntity;

public class OwnerMapper {

    public static OwnerJpaEntity toJpaEntity(Owner owner) {
        if (owner == null) return null;

        return new OwnerJpaEntity(
                owner.getId(),
                owner.getEmail(),
                owner.getName(),
                owner.getRestaurantName(),
                owner.getLastUpdate()
        );
    }

    public static Owner toDomain(OwnerJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        return new Owner(
                jpaEntity.getId(),
                jpaEntity.getEmail(),
                jpaEntity.getName(),
                jpaEntity.getLastUpdate(),
                jpaEntity.getRestaurantName()
        );
    }
}
