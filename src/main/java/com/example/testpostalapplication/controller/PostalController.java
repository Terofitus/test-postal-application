package com.example.testpostalapplication.controller;

import com.example.testpostalapplication.dto.PostalDispatchCreateDto;
import com.example.testpostalapplication.dto.PostalDispatchGetDto;
import com.example.testpostalapplication.dto.PostalDispatchHistoryDto;
import com.example.testpostalapplication.dto.PostalDispatchStatusDto;
import com.example.testpostalapplication.service.PostalHistoryService;
import com.example.testpostalapplication.service.PostalService;
import com.example.testpostalapplication.util.PageableCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = "Контроллер почтовых отправлений", description = "Содержит методы по добавлению и изменению состояний" +
        " почтовых отправлений")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostalController {
    private final PostalService postalService;
    private final PostalHistoryService postalHistoryService;

    @Operation(
            summary = "Регистрация почтового отправления"
    )
    @PostMapping(path = "/new-dispatch", produces = APPLICATION_JSON_VALUE)
    public PostalDispatchGetDto addNewDispatch(@Valid @RequestBody PostalDispatchCreateDto dispatchCreateDto) {
        return postalService.addNewDispatch(dispatchCreateDto);
    }

    @Operation(
            summary = "Регистрация отправления посылки из точки пребывания"
    )
    @PatchMapping(path = "/next-destination", produces = APPLICATION_JSON_VALUE)
    public PostalDispatchStatusDto dispatchToPostOffice(
            @RequestParam @Parameter(description = "Идентификатор посылки") Long dispatchId,
            @RequestParam @Parameter(description = "Идентификатор почтового отделения") Long postOfficeId) {
        return postalService.dispatchToPostOffice(dispatchId, postOfficeId);
    }

    @Operation(
            summary = "Подтверждение прибытия почтового отправления"
    )
    @PatchMapping(path = "/confirmation-arrival", produces = APPLICATION_JSON_VALUE)
    public PostalDispatchStatusDto confirmationOfArrival(
            @RequestParam @Parameter(description = "Идентификатор посылки") Long dispatchId,
            @RequestParam @Parameter(description = "Идентификатор почтового отделения") Long postOfficeId) {
        return postalService.confirmationOfArrival(dispatchId, postOfficeId);
    }

    @Operation(
            summary = "Подтверждение доставки почтового отправления получателю"
    )
    @PatchMapping(path = "/confirmation-receipt", produces = APPLICATION_JSON_VALUE)
    public PostalDispatchStatusDto confirmationOfReceipt(
            @RequestParam @Parameter(description = "Идентификатор посылки") Long dispatchId
    ) {
        return postalService.confirmationOfReceipt(dispatchId);
    }

    @Operation(
            summary = "Получение текущего статуса и местонахождения посылки"
    )
    @GetMapping(path = "/status/{dispatchId}", produces = APPLICATION_JSON_VALUE)
    public PostalDispatchStatusDto getStatusOfDispatch(
            @PathVariable @Parameter(description = "Идентификатор посылки") Long dispatchId
    ) {
        return postalHistoryService.getStatusOfDispatch(dispatchId);
    }

    @Operation(
            summary = "Получение истории изменений почтового отправления"
    )
    @GetMapping(path = "/history/{dispatchId}", produces = APPLICATION_JSON_VALUE)
    public List<PostalDispatchHistoryDto> getHistoryOfDispatch(
            @PathVariable @Parameter(description = "Идентификатор посылки") Long dispatchId,
            @RequestParam(required = false, defaultValue = "0") @Parameter(description = "Номер страницы пагинации")
            Integer from,
            @RequestParam(required = false, defaultValue = "10") @Parameter(description = "Количество " +
                    "элементов на странице") Integer size
    ) {
        return postalHistoryService.getHistoryOfDispatch(dispatchId, PageableCreator.toPageable(from, size, null));
    }
}
