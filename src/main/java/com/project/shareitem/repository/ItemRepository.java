package com.project.shareitem.repository;

import com.project.shareitem.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    String SEARCH_AVAILABLE_ITEMS = """ 
            SELECT item
            FROM Item item
            WHERE (LOWER(item.name) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(item.description) LIKE LOWER(CONCAT('%', :text, '%')))
            AND item.available = true
            """;

    List<Item> findAllByOwnerId(Long ownerId);

    @Query(SEARCH_AVAILABLE_ITEMS)
    List<Item> searchAvailableItems(@Param("text") String text);

    boolean existsByOwnerId(Long ownerId);
}
