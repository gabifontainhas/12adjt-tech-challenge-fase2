package br.com.gabifontainhas.techchallenge.application.usecase.customer;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.domain.entity.Customer;

import java.util.UUID;

public class ListCustomerByIdUseCase {
    private final CustomerRepository customerRepository;

    public ListCustomerByIdUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomersById(UUID uuid) {
        return customerRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException("Customer not found"));
    }
}