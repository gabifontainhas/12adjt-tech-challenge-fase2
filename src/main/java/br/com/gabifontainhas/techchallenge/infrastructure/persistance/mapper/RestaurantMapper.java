package br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper;

import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;
import br.com.gabifontainhas.techchallenge.domain.valueobjects.Address;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity.AddressEmbeddable;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity.RestaurantJpaEntity;

public final class RestaurantMapper {

    private RestaurantMapper() {
    }

    public static RestaurantJpaEntity toJpaEntity(Restaurant domain) {
        if (domain == null) return null;
        AddressEmbeddable addressEmbeddable = null;
        if (domain.getAddress() != null) {
            var address = domain.getAddress();
            addressEmbeddable = new AddressEmbeddable(
                    address.street(),
                    address.number(),
                    address.neighborhood(),
                    address.city(),
                    address.state(),
                    address.zipCode()
            );
        }
        return new RestaurantJpaEntity(
                domain.getId(),
                domain.getName(),
                domain.getCuisineType(),
                domain.getOperatingHours(),
                domain.getOwnerId(),
                addressEmbeddable
        );
    }

    public static Restaurant toDomain(RestaurantJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        Address domainAddress = null;
        if (jpaEntity.getAddress() != null) {
            var addressEmbeddable = jpaEntity.getAddress();
            domainAddress = new Address(
                    addressEmbeddable.getStreet(),
                    addressEmbeddable.getNumber(),
                    addressEmbeddable.getNeighborhood(),
                    addressEmbeddable.getCity(),
                    addressEmbeddable.getState(),
                    addressEmbeddable.getZipCode()
            );
        }

        return new Restaurant(
                jpaEntity.getId(),
                jpaEntity.getName(),
                domainAddress,
                jpaEntity.getCuisineType(),
                jpaEntity.getOperatingHours(),
                jpaEntity.getOwnerId()
        );
    }
}
