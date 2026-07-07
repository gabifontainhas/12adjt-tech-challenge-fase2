package br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper;

import br.com.gabifontainhas.techchallenge.domain.entities.Customer;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity.CustomerJpaEntity;

public class CustomerMapper {

    public static CustomerJpaEntity toJpaEntity(Customer customer) {
        if (customer == null) return null;

        return new CustomerJpaEntity(
                customer.getId(),
                customer.getEmail(),
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getLastUpdate()
        );
    }

    public static Customer toDomain(CustomerJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        return new Customer(
                jpaEntity.getId(),
                jpaEntity.getEmail(),
                jpaEntity.getName(),
                jpaEntity.getLastUpdate(),
                jpaEntity.getPhoneNumber()
        );
    }
}
