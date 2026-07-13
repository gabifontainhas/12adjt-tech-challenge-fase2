package br.com.gabifontainhas.techchallenge.infrastructure.gateways;

import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.MenuItem;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.MenuItemJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper.MenuItemMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MenuItemRepositoryAdapter implements MenuItemRepository {
    private final MenuItemJpaRepository menuItemJpaRepository;

    public MenuItemRepositoryAdapter(MenuItemJpaRepository menuItemJpaRepository) {
        this.menuItemJpaRepository = menuItemJpaRepository;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        var savedEntity = menuItemJpaRepository.save(MenuItemMapper.toJpaEntity(menuItem));
        return MenuItemMapper.toDomain(savedEntity);
    }

    @Override
    public List<MenuItem> findAll() {
        return menuItemJpaRepository.findAll().stream()
                .map(MenuItemMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<MenuItem> findById(UUID id) {
        return menuItemJpaRepository.findById(id).map(MenuItemMapper::toDomain);
    }
    
    @Override
    public void delete(UUID id) {
        this.menuItemJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.menuItemJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByNameAndRestaurantId(String name, UUID id) {
        return this.menuItemJpaRepository.existsByNameAndRestaurantId(name, id);
    }

    @Override
    public List<MenuItem> findByRestaurantId(UUID id) {
        return menuItemJpaRepository.findByRestaurantId(id).stream()
                .map(MenuItemMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteByRestaurantId(UUID restaurantId) {
        this.menuItemJpaRepository.deleteByRestaurantId(restaurantId);
    }
}
