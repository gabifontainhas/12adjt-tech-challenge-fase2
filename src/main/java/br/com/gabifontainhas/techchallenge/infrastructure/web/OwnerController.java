package br.com.gabifontainhas.techchallenge.infrastructure.web;

import br.com.gabifontainhas.techchallenge.application.usecases.dto.OwnerDTO;
import br.com.gabifontainhas.techchallenge.application.usecases.owner.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/owners")
public class OwnerController {
    private final CreateOwnerUseCase createOwnerUseCase;
    private final ListOwnersUseCase listOwnersUseCase;
    private final DeleteOwnerUseCase deleteOwnerUseCase;
    private final UpdateOwnerUseCase updateOwnerUseCase;

    public OwnerController(
            CreateOwnerUseCase createOwnerUseCase,
            ListOwnersUseCase listOwnersUseCase,
            DeleteOwnerUseCase deleteOwnerUseCase,
            UpdateOwnerUseCase updateOwnerUseCase
    ) {
        this.createOwnerUseCase = createOwnerUseCase;
        this.listOwnersUseCase = listOwnersUseCase;
        this.deleteOwnerUseCase = deleteOwnerUseCase;
        this.updateOwnerUseCase = updateOwnerUseCase;
    }

    @PostMapping
    public ResponseEntity<OwnerDTO.Response> create(@RequestBody OwnerDTO.PostRequest dto) {
        var owner = this.createOwnerUseCase.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OwnerDTO.Response(owner));
    }

    @GetMapping
    public ResponseEntity<List<OwnerDTO.Response>> listOwners() {
        var ownerList = listOwnersUseCase.getAllOwners();

        var response = ownerList.stream()
                .map(OwnerDTO.Response::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDTO.Response> listCustomerById(@PathVariable UUID id) {
        var customer = listOwnersUseCase.getOwnerById(id);
        return ResponseEntity.ok(new OwnerDTO.Response(customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwnerById(@PathVariable UUID id) {
        deleteOwnerUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerDTO.Response> update(@PathVariable UUID id, @RequestBody OwnerDTO.PutRequest dto) {
        var owner = this.updateOwnerUseCase.update(dto, id);
        return ResponseEntity.ok(new OwnerDTO.Response(owner));
    }
}

