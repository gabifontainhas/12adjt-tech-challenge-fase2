package br.com.gabifontainhas.techchallenge.application.usecases.customer;

import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Customer;

import java.util.List;

public class ListCustomersUseCase {
    private final CustomerRepository customerRepository;

    public ListCustomersUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}