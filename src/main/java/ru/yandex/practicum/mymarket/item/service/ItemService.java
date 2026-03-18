package ru.yandex.practicum.mymarket.item.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.mymarket.item.dto.ItemDto;
import ru.yandex.practicum.mymarket.item.model.Item;
import ru.yandex.practicum.mymarket.item.repository.ItemRepository;

import java.util.List;
import java.util.Map;

public interface ItemService {
    Page<Item> findItems(String search, String sort, Pageable pageable);
    Item findById(Long id);
    List<ItemDto> convertItemsToCartDtos(Map<Long, Integer> cartItems);
    int resolveCountInCart(Map<Long, Integer> cartItems, Long itemId);
}
