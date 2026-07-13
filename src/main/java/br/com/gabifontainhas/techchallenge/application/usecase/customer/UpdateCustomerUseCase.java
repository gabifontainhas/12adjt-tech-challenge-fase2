package br.com.gabifontainhas.techchallenge.application.usecase.customer;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.application.usecase.dto.UpdateCustomerCommand;
import br.com.gabifontainhas.techchallenge.domain.entity.Customer;

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