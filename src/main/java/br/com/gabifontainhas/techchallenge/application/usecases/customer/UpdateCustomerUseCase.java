package br.com.gabifontainhas.techchallenge.application.usecases.customer;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.UpdateCustomerCommand;
import br.com.gabifontainhas.techchallenge.domain.entities.Customer;

import java.util.UUID;

public class UpdateCustomerUseCase {
    private final CustomerRepository customerRepository;

    public UpdateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer update(UpdateCustomerCommand request, UUID id) {
        var customer = customerRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Customer not found"));
        customer.update(request.name(), request.phoneNumber());
        return customerRepository.save(customer);
    }
}