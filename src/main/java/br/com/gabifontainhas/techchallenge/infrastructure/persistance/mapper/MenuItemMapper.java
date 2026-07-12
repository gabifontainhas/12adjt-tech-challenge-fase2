package br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper;

import br.com.gabifontainhas.techchallenge.domain.entities.MenuItem;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity.MenuItemJpaEntity;

public final class MenuItemMapper {

    private MenuItemMapper() {
    }

    public static MenuItemJpaEntity toJpaEntity(MenuItem domain) {
        if (domain == null) return null;
        return new MenuItemJpaEntity(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getPrice(),
                domain.isDineInOnly(),
                domain.getImagePath(),
                domain.getRestaurantId()
        );
    }

    public static MenuItem toDomain(MenuItemJpaEntity entity) {
        if (entity == null) return null;
        return new MenuItem(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.isDineInOnly(),
                entity.getImagePath(),
                entity.getRestaurantId()
        );
    }
}
