package br.com.gabifontainhas.techchallenge.infrastructure.web;

import br.com.gabifontainhas.techchallenge.application.usecases.restaurant.CreateRestaurantUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.restaurant.DeleteRestaurantUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.restaurant.ListRestaurantsUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.restaurant.UpdateRestaurantUseCase;
import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.RestaurantDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/restaurants")
public class RestaurantController {
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final ListRestaurantsUseCase listRestaurantsUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;

    public RestaurantController(
            CreateRestaurantUseCase createRestaurantUseCase,
            ListRestaurantsUseCase listRestaurantsUseCase,
            DeleteRestaurantUseCase deleteRestaurantUseCase,
            UpdateRestaurantUseCase updateRestaurantUseCase
    ) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.listRestaurantsUseCase = listRestaurantsUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
    }

    @PostMapping
    public ResponseEntity<RestaurantDTO.Response> create(@RequestBody @Valid RestaurantDTO.PostRequest dto) {
        var restaurant = this.createRestaurantUseCase.create(dto.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantDTO.Response(restaurant));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO.Response>> listRestaurants() {
        var restaurantList = listRestaurantsUseCase.getAllRestaurants();

        var response = restaurantList.stream()
                .map(RestaurantDTO.Response::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO.Response> listRestaurantById(@PathVariable UUID id) {
        var restaurant = listRestaurantsUseCase.getRestaurantById(id);
        return ResponseEntity.ok(new RestaurantDTO.Response(restaurant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantById(@PathVariable UUID id) {
        deleteRestaurantUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO.Response> update(@PathVariable UUID id, @RequestBody  @Valid RestaurantDTO.PutRequest dto) {
        var restaurant = this.updateRestaurantUseCase.update(dto.toCommand(), id);
        return ResponseEntity.ok(new RestaurantDTO.Response(restaurant));
    }

}
