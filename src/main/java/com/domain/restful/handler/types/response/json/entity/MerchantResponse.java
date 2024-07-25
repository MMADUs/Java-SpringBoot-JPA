package com.domain.restful.handler.types.response.json.entity;

import com.domain.restful.handler.types.response.json.type.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantResponse {
    private Long id;
    private String name;
    private String location;
    private List<Product> products;
}
