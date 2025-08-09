package org.hrd.hibernatejpa01.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    @NotNull(message = "Not blank")
    @Size(min = 1, max = 100, message = "Size must be between 1 and 100")
    private String name;

    @NotNull(message = "Not blank")
    @Positive(message = "Not negative")
    private Double price;

    @NotNull(message = "Not blank")
    @PositiveOrZero(message = "Not negative")
    private Integer quantity;
}
