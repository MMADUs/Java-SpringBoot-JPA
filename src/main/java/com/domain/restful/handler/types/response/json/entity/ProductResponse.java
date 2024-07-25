package com.domain.restful.handler.types.response.json.entity;

import com.domain.restful.handler.types.response.json.type.Merchant;
import com.domain.restful.handler.types.response.json.type.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Boolean available;
    private Category category;
    private Merchant merchant;
}
