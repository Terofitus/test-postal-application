package com.example.testpostalapplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostalDispatchCreateDto {
    private Long destinationId;
    @NotNull
    @NotBlank
    @Size(min = 6, max = 100)
    @Schema(description = "Имя получателя")
    private String recipientName;
    @NotNull
    @NotBlank
    @Schema(description = "Тип посылки")
    private String type;
}
