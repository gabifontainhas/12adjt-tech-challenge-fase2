package br.com.gabifontainhas.techchallenge.infrastructure.persistence.mapper;

import br.com.gabifontainhas.techchallenge.domain.entity.Owner;
import br.com.gabifontainhas.techchallenge.infrastructure.persistence.entity.OwnerJpaEntity;

public final class OwnerMapper {

    private OwnerMapper() {
    }

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
                jpaEntity.getBusinessPhone()
        );
    }
}
