package com.example.testpostalapplication.service;

import com.example.testpostalapplication.dto.PostalDispatchHistoryDto;
import com.example.testpostalapplication.dto.PostalDispatchStatusDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostalHistoryService {
    PostalDispatchStatusDto getStatusOfDispatch(Long dispatchId);

    List<PostalDispatchHistoryDto> getHistoryOfDispatch(Long dispatchId, Pageable pageable);
}
