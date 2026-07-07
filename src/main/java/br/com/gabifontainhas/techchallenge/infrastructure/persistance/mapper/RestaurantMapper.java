package br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper;

import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;
import br.com.gabifontainhas.techchallenge.domain.valueobjects.Address;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity.AddressEmbeddable;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity.RestaurantJpaEntity;

public class RestaurantMapper {
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

    public static Restaurant toDomain(RestaurantJpaEntity entity) {
        if (entity == null) return null;

        Address domainAddress = null;
        if (entity.getAddress() != null) {
            var addressEmbeddable = entity.getAddress();
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
                entity.getId(),
                entity.getName(),
                domainAddress,
                entity.getCuisineType(),
                entity.getOperatingHours(),
                entity.getOwnerId()
        );
    }
}
