package ru.yandex.practicum.mymarket.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mymarket.exception.ItemNotFoundException;
import ru.yandex.practicum.mymarket.item.dto.ItemDto;
import ru.yandex.practicum.mymarket.item.mapper.ItemMapper;
import ru.yandex.practicum.mymarket.item.model.Item;
import ru.yandex.practicum.mymarket.item.repository.ItemRepository;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public Page<Item> findItems(String search, String sort, Pageable pageable){
        Pageable sortedPageable = applySort(pageable,sort);
        if(search != null && !search.isBlank()){
            return itemRepository.searchByTitleOrDescription(search.trim(), sortedPageable);
        }
        return itemRepository.findAll(sortedPageable);
    }

    private Pageable applySort(Pageable pageable, String sort) {
        if ("ALPHA".equalsIgnoreCase(sort)) {
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("title").ascending());
        } else if ("PRICE".equalsIgnoreCase(sort)) {
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").ascending());
        }
        return pageable;
    }

    @Override
    public Item findById(Long id){
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found with id" + id));
    }

    @Override
    public List<ItemDto> convertItemsToCartDtos(Map<Long, Integer> cartItems) {
        return cartItems.entrySet().stream()
                .map(entry -> {
                    Item item = itemRepository.findById(entry.getKey())
                            .orElseThrow(() -> new ItemNotFoundException("Item not found"));
                    ItemDto dto = itemMapper.toDto(item);
                    dto.setCount(entry.getValue());
                    return dto;
                })
                .toList();
    }

    @Override
    public int resolveCountInCart(Map<Long, Integer> cartItems, Long itemId) {
        return cartItems.getOrDefault(itemId, 0);
    }
}
