package br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper;

import br.com.gabifontainhas.techchallenge.domain.entities.Owner;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity.OwnerJpaEntity;

public class OwnerMapper {

    public static OwnerJpaEntity toJpaEntity(Owner domain) {
        if (domain == null) return null;

        return new OwnerJpaEntity(
                domain.getId(),
                domain.getEmail(),
                domain.getName(),
                domain.getBusinessPhone(),
                domain.getLastUpdate()
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
