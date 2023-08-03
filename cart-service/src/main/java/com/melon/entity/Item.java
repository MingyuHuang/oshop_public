package com.melon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash
public class Item {

    private String id;
    private String imageUrl;
    private Integer quantity;
    private Double price;
    private String title;
}
