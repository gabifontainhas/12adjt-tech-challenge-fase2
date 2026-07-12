package br.com.gabifontainhas.techchallenge.infrastructure.web;


import br.com.gabifontainhas.techchallenge.application.usecases.menuitem.*;
import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.MenuItemDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/menuItems")
public class MenuItemController {
    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final ListMenuItemsUseCase listMenuItemsUseCase;
    private final ListMenuItemByIdUseCase listMenuItemByIdUseCase;
    private final ListMenuItemByRestaurantIdUseCase listMenuItemByRestaurantIdUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;

    public MenuItemController(CreateMenuItemUseCase createMenuItemUseCase,
                              ListMenuItemsUseCase listMenuItemsUseCase,
                              ListMenuItemByIdUseCase listMenuItemByIdUseCase,
                              ListMenuItemByRestaurantIdUseCase listMenuItemByRestaurantIdUseCase,
                              DeleteMenuItemUseCase deleteMenuItemUseCase,
                              UpdateMenuItemUseCase updateMenuItemUseCase) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.listMenuItemsUseCase = listMenuItemsUseCase;
        this.listMenuItemByIdUseCase = listMenuItemByIdUseCase;
        this.listMenuItemByRestaurantIdUseCase = listMenuItemByRestaurantIdUseCase;
        this.deleteMenuItemUseCase = deleteMenuItemUseCase;
        this.updateMenuItemUseCase = updateMenuItemUseCase;
    }

    @PostMapping
    public ResponseEntity<MenuItemDTO.Response> create(@RequestBody @Valid MenuItemDTO.PostRequest dto) {
        var menuItem = this.createMenuItemUseCase.create(dto.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MenuItemDTO.Response(menuItem));
    }

    @GetMapping
    public ResponseEntity<List<MenuItemDTO.Response>> listMenuItems() {
        var menuItemList = listMenuItemsUseCase.getAllMenuItems();
        var response = menuItemList.stream()
                .map(MenuItemDTO.Response::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO.Response> listMenuItemById(@PathVariable UUID id) {
        var menuItem = listMenuItemByIdUseCase.getMenuItemById(id);
        return ResponseEntity.ok(new MenuItemDTO.Response(menuItem));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemDTO.Response>> ListByRestaurantId(@PathVariable UUID restaurantId) {
        var menuItemList = listMenuItemByRestaurantIdUseCase.getAllMenuItemsByRestaurant(restaurantId);

        var response = menuItemList.stream()
                .map(MenuItemDTO.Response::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItemById(@PathVariable UUID id) {
        deleteMenuItemUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO.Response> update(@PathVariable UUID id, @RequestBody @Valid MenuItemDTO.PutRequest dto) {
        var menuItem = this.updateMenuItemUseCase.update(dto.toCommand(), id);
        return ResponseEntity.ok(new MenuItemDTO.Response(menuItem));
    }

}
