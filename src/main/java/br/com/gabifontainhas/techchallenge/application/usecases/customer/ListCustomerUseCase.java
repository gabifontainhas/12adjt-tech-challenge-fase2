package br.com.gabifontainhas.techchallenge.application.usecases.customer;

import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Customer;

import java.util.List;
import java.util.UUID;

public class ListCustomerUseCase {
    private final CustomerRepository customerRepository;

    public ListCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomersById(UUID uuid) {
        return customerRepository.findById(uuid);
    }

}