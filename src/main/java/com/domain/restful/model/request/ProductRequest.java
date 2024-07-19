package com.domain.restful.model.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
  @NotBlank(message = "Name is required")
  @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
  private String name;

  @NotBlank(message = "Description is required")
  @Size(min = 20, max = 100, message = "Description must be between 20 and 100 characters")
  private String description;

  @NotNull(message = "Price is required")
  @DecimalMin(value = "0.01", message = "Price must be greater than 0")
  @DecimalMax(value = "9999999.99", message = "Price is too high")
  private BigDecimal price;

  @NotNull(message = "Stock is required")
  @Min(value = 0, message = "Stock cannot be negative")
  @Max(value = 99999, message = "Stock is too high")
  private Integer stock;

  @NotNull(message = "Availability is required")
  private Boolean available;
}
