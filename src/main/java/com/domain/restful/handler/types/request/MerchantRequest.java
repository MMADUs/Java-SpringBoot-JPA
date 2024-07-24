package com.domain.restful.handler.types.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantRequest {
  @NotBlank(message = "Name is required")
  @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
  private String name;

  @NotBlank(message = "Location is required")
  @Size(min = 5, max = 30, message = "Location must be between 5 and 30 characters")
  private String location;
}
