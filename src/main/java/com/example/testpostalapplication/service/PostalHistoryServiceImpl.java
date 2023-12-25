package com.example.testpostalapplication.service;

import com.example.testpostalapplication.dto.PostalDispatchHistoryDto;
import com.example.testpostalapplication.dto.PostalDispatchStatusDto;
import com.example.testpostalapplication.exception.ObjectNotFoundException;
import com.example.testpostalapplication.model.PostalDispatch;
import com.example.testpostalapplication.model.PostalDispatchHistory;
import com.example.testpostalapplication.repository.DispatchHistoryRepository;
import com.example.testpostalapplication.repository.DispatchRepository;
import com.example.testpostalapplication.util.DispatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostalHistoryServiceImpl implements PostalHistoryService {
    private final DispatchHistoryRepository historyRepository;
    private final DispatchRepository dispatchRepository;

    @Override
    public PostalDispatchStatusDto getStatusOfDispatch(Long dispatchId) {
        PostalDispatch postalDispatch = dispatchRepository.findById(dispatchId).orElseThrow(
                () -> new ObjectNotFoundException("Postal dispatch with id=" + dispatchId + " not found.")
        );

        PostalDispatchHistory dispatchHistory = historyRepository.lastTransfer(dispatchId).orElseThrow(
                () -> new ObjectNotFoundException("The postal dispatch history not found"));

        return DispatchMapper.postalDispatchStatusDto(postalDispatch, dispatchHistory);
    }

    @Override
    public List<PostalDispatchHistoryDto> getHistoryOfDispatch(Long dispatchId, Pageable pageable) {
        Page<PostalDispatchHistory> dispatchHistory = historyRepository.
                findAllByPostalDispatchIdOrderByDateTime(dispatchId, pageable);

        if (dispatchHistory.isEmpty()) {
            throw new ObjectNotFoundException("No history was found for the postal dispatch with an id=" + dispatchId);
        }

        return DispatchMapper.postalDispatchHistoryDto(dispatchHistory);
    }
}
