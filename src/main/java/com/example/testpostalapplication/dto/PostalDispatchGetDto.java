package com.example.testpostalapplication.dto;


import com.example.testpostalapplication.model.PostalDispatchStatus;
import com.example.testpostalapplication.model.PostalDispatchType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostalDispatchGetDto {
    private Long id;
    @Schema(description = "Имя получателя")
    private String recipientName;
    @Schema(description = "Тип посылки")
    private PostalDispatchType type;
    @Schema(description = "Точка назначения")
    private PostOfficeDto destination;
    @Schema(description = "Статус отправления")
    private PostalDispatchStatus status;
}
