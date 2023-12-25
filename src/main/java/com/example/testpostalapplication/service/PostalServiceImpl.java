package com.example.testpostalapplication.service;

import com.example.testpostalapplication.dto.PostalDispatchCreateDto;
import com.example.testpostalapplication.dto.PostalDispatchGetDto;
import com.example.testpostalapplication.dto.PostalDispatchStatusDto;
import com.example.testpostalapplication.exception.ObjectNotFoundException;
import com.example.testpostalapplication.model.PostOffice;
import com.example.testpostalapplication.model.PostalDispatch;
import com.example.testpostalapplication.model.PostalDispatchHistory;
import com.example.testpostalapplication.model.PostalDispatchStatus;
import com.example.testpostalapplication.repository.DispatchHistoryRepository;
import com.example.testpostalapplication.repository.DispatchRepository;
import com.example.testpostalapplication.repository.PostOfficeRepository;
import com.example.testpostalapplication.util.DispatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostalServiceImpl implements PostalService {
    private final DispatchRepository dispatchRepository;
    private final PostOfficeRepository officeRepository;
    private final DispatchHistoryRepository historyRepository;

    @Transactional
    @Override
    public PostalDispatchGetDto addNewDispatch(PostalDispatchCreateDto createDto) {
        PostOffice postOffice = officeRepository.findById(createDto.getDestinationId()).orElseThrow(
                () -> new ObjectNotFoundException("Post office with id=" + createDto.getDestinationId() + " not found.")
        );

        PostalDispatch postalDispatch = DispatchMapper.postalDispatch(createDto, postOffice);
        PostalDispatch savedDispatch = dispatchRepository.save(postalDispatch);

        return DispatchMapper.postalDispatchGetDto(savedDispatch);
    }

    @Transactional
    @Override
    public PostalDispatchStatusDto dispatchToPostOffice(Long dispatchId, Long postOfficeId) {
        PostOffice destinationPostOffice = officeRepository.findById(postOfficeId).orElseThrow(
                () -> new ObjectNotFoundException("Post office with id=" + postOfficeId + " not found.")
        );

        PostalDispatch postalDispatch = dispatchRepository.findById(dispatchId).orElseThrow(
                () -> new ObjectNotFoundException("Postal dispatch with id=" + dispatchId + " not found.")
        );

        PostalDispatchHistory dispatchHistory = switch (postalDispatch.getStatus()) {
            case REGISTERED, AT_INTERMEDIATE_POINT -> DispatchMapper.postalDispatchHistory(postalDispatch,
                    destinationPostOffice, PostalDispatchStatus.IN_TRANSIT);
            case DELIVERED -> throw new IllegalArgumentException("The postal dispatch is waiting for the recipient");
            case IN_TRANSIT -> throw new IllegalArgumentException("The postal dispatch is still on the way");
            case RECEIVED -> throw new IllegalArgumentException("The postal dispatch has already been delivered");
        };

        postalDispatch.setStatus(PostalDispatchStatus.IN_TRANSIT);
        dispatchRepository.save(postalDispatch);

        PostalDispatchHistory savedHistory = historyRepository.save(dispatchHistory);
        return DispatchMapper.postalDispatchStatusDto(postalDispatch, savedHistory);
    }

    @Transactional
    @Override
    public PostalDispatchStatusDto confirmationOfArrival(Long dispatchId, Long postOfficeId) {
        PostOffice destinationPostOffice = officeRepository.findById(postOfficeId).orElseThrow(
                () -> new ObjectNotFoundException("Post office with id=" + postOfficeId + " not found.")
        );

        PostalDispatch postalDispatch = dispatchRepository.findById(dispatchId).orElseThrow(
                () -> new ObjectNotFoundException("Postal dispatch with id=" + dispatchId + " not found.")
        );

        PostalDispatchHistory dispatchHistory = historyRepository.lastTransfer(dispatchId).orElseThrow(
                () -> new IllegalArgumentException("The postal dispatch is not on the way")
        );

        if (postalDispatch.getStatus() != PostalDispatchStatus.IN_TRANSIT ||
                !dispatchHistory.getCurrentDestination().equals(destinationPostOffice)) {
            throw new IllegalArgumentException("The postal dispatch is not on the way to the destination");
        }

        PostalDispatchHistory newHistory = DispatchMapper.postalDispatchHistory(postalDispatch, destinationPostOffice,
                null);

        if (postalDispatch.getDestination().getId().equals(postOfficeId)) {
            postalDispatch.setStatus(PostalDispatchStatus.DELIVERED);
            newHistory.setStatus(PostalDispatchStatus.DELIVERED);
        } else {
            postalDispatch.setStatus(PostalDispatchStatus.AT_INTERMEDIATE_POINT);
            newHistory.setStatus(PostalDispatchStatus.AT_INTERMEDIATE_POINT);
        }
        dispatchRepository.save(postalDispatch);

        PostalDispatchHistory savedHistory = historyRepository.save(newHistory);
        return DispatchMapper.postalDispatchStatusDto(postalDispatch, savedHistory);
    }

    @Transactional
    @Override
    public PostalDispatchStatusDto confirmationOfReceipt(Long dispatchId) {
        PostalDispatch postalDispatch = dispatchRepository.findById(dispatchId).orElseThrow(
                () -> new ObjectNotFoundException("Postal dispatch with id=" + dispatchId + " not found.")
        );

        PostalDispatchHistory dispatchHistory = historyRepository.lastTransfer(dispatchId).orElseThrow(
                () -> new IllegalArgumentException("The postal dispatch has not been delivered.")
        );

        if (postalDispatch.getDestination().getId().equals(dispatchHistory.getCurrentDestination().getId()) &&
                dispatchHistory.getStatus().equals(PostalDispatchStatus.DELIVERED)) {
            postalDispatch.setStatus(PostalDispatchStatus.RECEIVED);
            dispatchRepository.save(postalDispatch);
        } else {
            throw new IllegalArgumentException("The postal dispatch has not been delivered.");
        }

        PostalDispatchHistory newHistory = DispatchMapper.postalDispatchHistory(postalDispatch,
                postalDispatch.getDestination(),
                PostalDispatchStatus.RECEIVED);

        PostalDispatchHistory savedHistory = historyRepository.save(newHistory);
        return DispatchMapper.postalDispatchStatusDto(postalDispatch, savedHistory);
    }
}
