package ru.yandex.practicum.mymarket.item.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    private String title;
    private String description;
    private String imgPath;
    private long price;
}
