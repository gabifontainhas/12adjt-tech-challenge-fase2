package br.com.gabifontainhas.techchallenge.application.usecase.customer;

import br.com.gabifontainhas.techchallenge.application.exception.EmailAlreadyExistsException;
import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.application.usecase.dto.CreateCustomerCommand;
import br.com.gabifontainhas.techchallenge.domain.entity.Customer;

public class CreateCustomerUseCase {
    private final CustomerRepository customerRepository;

    public CreateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer create(CreateCustomerCommand request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("E-mail already exists");
        }
        var customer = new Customer(request.email(), request.name(), request.phoneNumber());
        return customerRepository.save(customer);
    }
}