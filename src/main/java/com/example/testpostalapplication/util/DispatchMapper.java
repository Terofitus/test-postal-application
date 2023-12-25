package com.example.testpostalapplication.util;

import com.example.testpostalapplication.dto.*;
import com.example.testpostalapplication.model.*;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DispatchMapper {
    public PostalDispatch postalDispatch(PostalDispatchCreateDto createDto, PostOffice destination) {
        return new PostalDispatch(null,
                createDto.getRecipientName(),
                PostalDispatchType.valueOf(createDto.getType()),
                destination,
                PostalDispatchStatus.REGISTERED);
    }

    public PostalDispatchHistory postalDispatchHistory(PostalDispatch postalDispatch, PostOffice postOffice,
                                                       PostalDispatchStatus status) {
        return new PostalDispatchHistory(null, postalDispatch, postOffice, status, LocalDateTime.now());
    }

    public PostalDispatchGetDto postalDispatchGetDto(PostalDispatch postalDispatch) {
        return new PostalDispatchGetDto(postalDispatch.getId(),
                postalDispatch.getRecipientName(),
                postalDispatch.getType(),
                new PostOfficeDto(postalDispatch.getDestination()),
                postalDispatch.getStatus());
    }

    public List<PostalDispatchHistoryDto> postalDispatchHistoryDto(Page<PostalDispatchHistory> history) {
        return history.stream().map(dispatchHistory -> new PostalDispatchHistoryDto(dispatchHistory.getId(),
                new PostOfficeDto(dispatchHistory.getCurrentDestination()),
                dispatchHistory.getStatus(),
                dispatchHistory.getDateTime())).collect(Collectors.toList());
    }

    public PostalDispatchStatusDto postalDispatchStatusDto(PostalDispatch postalDispatch,
                                                           PostalDispatchHistory dispatchHistory) {
        return new PostalDispatchStatusDto(postalDispatch.getId(),
                postalDispatch.getRecipientName(),
                new PostOfficeDto(dispatchHistory.getCurrentDestination()),
                new PostOfficeDto(postalDispatch.getDestination()),
                dispatchHistory.getStatus(),
                dispatchHistory.getDateTime());
    }
}
