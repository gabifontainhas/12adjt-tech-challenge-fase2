package br.com.gabifontainhas.techchallenge.infrastructure.web;

import br.com.gabifontainhas.techchallenge.application.usecases.customer.CreateCustomerUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.customer.DeleteCustomerUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.customer.ListCustomersUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.customer.UpdateCustomerUseCase;

import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.CustomerDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
    private final CreateCustomerUseCase createCustomerUseCase;
    private final ListCustomersUseCase listCustomersUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;

    public CustomerController(
            CreateCustomerUseCase createCustomerUseCase,
            ListCustomersUseCase listCustomersUseCase,
            DeleteCustomerUseCase deleteCustomerUseCase,
            UpdateCustomerUseCase updateCustomerUseCase
    ) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.listCustomersUseCase = listCustomersUseCase;
        this.deleteCustomerUseCase = deleteCustomerUseCase;
        this.updateCustomerUseCase = updateCustomerUseCase;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO.Response> createCustomer(@RequestBody @Valid CustomerDTO.PostRequest dto) {
        var customer = this.createCustomerUseCase.create(dto.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CustomerDTO.Response(customer));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO.Response>> listCustomers() {
        var customerList = listCustomersUseCase.getAllCustomers();

        var response = customerList.stream()
                .map(CustomerDTO.Response::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO.Response> listCustomerById(@PathVariable UUID id) {
        var customer = listCustomersUseCase.getCustomersById(id);
        return ResponseEntity.ok(new CustomerDTO.Response(customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable UUID id) {
        deleteCustomerUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO.Response> update(@PathVariable UUID id, @RequestBody @Valid CustomerDTO.PutRequest dto) {
        var customer = this.updateCustomerUseCase.update(dto.toCommand(), id);
        return ResponseEntity.ok(new CustomerDTO.Response(customer));
    }
}
