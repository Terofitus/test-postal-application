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
public class PostalDispatchHistoryDto {
    private Long id;
    @Schema(description = "Текущая точка назначения")
    private PostOfficeDto currentDestination;
    @Schema(description = "Статус отправления")
    private PostalDispatchStatus status;
    @Schema(description = "Время совершения операции")
    private LocalDateTime dateTime;
}
