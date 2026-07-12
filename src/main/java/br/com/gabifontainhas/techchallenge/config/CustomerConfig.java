package br.com.gabifontainhas.techchallenge.config;

import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.customer.*;
import br.com.gabifontainhas.techchallenge.infrastructure.gateways.CustomerRepositoryAdapter;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.CustomerJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerConfig {
    @Bean
    public CreateCustomerUseCase createCustomerUseCase(CustomerRepository customerRepository) {
        return new CreateCustomerUseCase(customerRepository);
    }

    @Bean
    public ListCustomersUseCase listCustomersUseCase(CustomerRepository customerRepository) {
        return new ListCustomersUseCase(customerRepository);
    }

    @Bean
    public ListCustomerByIdUseCase listCustomerByIdUseCase(CustomerRepository customerRepository) {
        return new ListCustomerByIdUseCase(customerRepository);
    }

    @Bean
    public DeleteCustomerUseCase deleteCustomerUseCase(CustomerRepository customerRepository) {
        return new DeleteCustomerUseCase(customerRepository);
    }

    @Bean
    public UpdateCustomerUseCase updateCustomerUseCase(CustomerRepository customerRepository) {
        return new UpdateCustomerUseCase(customerRepository);
    }

    @Bean
    public CustomerRepositoryAdapter createCustomerRepositoryAdapter(CustomerJpaRepository customerJpaRepository) {
        return new CustomerRepositoryAdapter(customerJpaRepository);
    }
}
