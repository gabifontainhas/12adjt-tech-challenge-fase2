package br.com.gabifontainhas.techchallenge.infrastructure.gateways;

import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Customer;
import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.CustomerJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper.CustomerMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerRepositoryAdapter(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerJpaRepository.existsByEmail(email);
    }

    @Override
    public List<Customer> findAll() {
        return customerJpaRepository.findAll().stream()
                .map(CustomerMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Customer findById(UUID id) {
        var customerEntity = customerJpaRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Customer not found"));
        return CustomerMapper.toDomain(customerEntity);
    }

    @Override
    public void delete(UUID id) {
        this.customerJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.customerJpaRepository.existsById(id);
    }


    @Override
    public Customer save(Customer customer) {
        var savedEntity = customerJpaRepository.save(CustomerMapper.toJpaEntity(customer));
        return CustomerMapper.toDomain(savedEntity);
    }

}
