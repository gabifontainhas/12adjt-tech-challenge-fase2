package br.com.gabifontainhas.techchallenge.application.gateway;

import br.com.gabifontainhas.techchallenge.domain.entities.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Customer save(Customer customer);

    boolean existsByEmail(String email);

    List<Customer> findAll();

    Optional<Customer> findById(UUID uuid);

    void delete(UUID id);

    boolean existsById(UUID id);
}
