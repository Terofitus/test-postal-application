package com.example.testpostalapplication.dto;

import com.example.testpostalapplication.model.PostalDispatchStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostalDispatchStatusDto {
    private Long id;
    @Schema(description = "Имя получателя")
    private String receiverName;
    @Schema(description = "Текущая точка назначения")
    private PostOfficeDto currentDestination;
    @Schema(description = "Конечная точка назначения")
    private PostOfficeDto finalDestination;
    @Schema(description = "Статус отправления")
    private PostalDispatchStatus status;
    @Schema(description = "Время совершения операции")
    private LocalDateTime timeOfOperation;
}
