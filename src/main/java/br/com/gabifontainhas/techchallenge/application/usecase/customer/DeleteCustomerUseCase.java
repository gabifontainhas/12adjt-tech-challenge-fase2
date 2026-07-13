package br.com.gabifontainhas.techchallenge.application.usecase.customer;

import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;

import java.util.UUID;

public class DeleteCustomerUseCase {

    private final CustomerRepository customerRepository;

    public DeleteCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void delete(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new UserNotFoundException("Could not delete: Customer with ID " + id + " not found");
        }
        customerRepository.delete(id);
    }
}
