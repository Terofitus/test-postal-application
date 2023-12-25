package com.example.testpostalapplication.service;

import com.example.testpostalapplication.dto.PostalDispatchCreateDto;
import com.example.testpostalapplication.dto.PostalDispatchGetDto;
import com.example.testpostalapplication.dto.PostalDispatchStatusDto;
import com.example.testpostalapplication.exception.ObjectNotFoundException;
import com.example.testpostalapplication.model.*;
import com.example.testpostalapplication.repository.DispatchHistoryRepository;
import com.example.testpostalapplication.repository.DispatchRepository;
import com.example.testpostalapplication.repository.PostOfficeRepository;
import com.example.testpostalapplication.util.DispatchMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PostalServiceImplTest {
    private final EasyRandom generator = new EasyRandom();
    @InjectMocks
    private PostalServiceImpl postalService;
    @Mock
    private DispatchRepository dispatchRepository;
    @Mock
    private PostOfficeRepository officeRepository;
    @Mock
    private DispatchHistoryRepository historyRepository;

    @Test
    protected void test_addNewDispatch_whenCreateDtoCorrect_shouldReturnSavedDispatch() {
        PostOffice postOffice = generator.nextObject(PostOffice.class);
        postOffice.setId(1L);
        PostalDispatchCreateDto createDto = generator.nextObject(PostalDispatchCreateDto.class);
        createDto.setDestinationId(1L);
        createDto.setType(PostalDispatchType.LETTER.name());
        PostalDispatch postalDispatch = DispatchMapper.postalDispatch(createDto, postOffice);
        Mockito.when(officeRepository.findById(1L)).thenReturn(Optional.of(postOffice));
        Mockito.when(dispatchRepository.save(postalDispatch)).thenReturn(postalDispatch);

        PostalDispatchGetDto dispatchDtoFromService = postalService.addNewDispatch(createDto);
        PostalDispatchGetDto dispatchGetDto = DispatchMapper.postalDispatchGetDto(postalDispatch);

        assertEquals(dispatchGetDto, dispatchDtoFromService,
                "The dto from the service does not match what is expected");
    }

    @Test
    protected void test_dispatchToPostOffice_whenPostOfficeNotFound_shouldThrowException() {
        Mockito.when(officeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> postalService.dispatchToPostOffice(1L, 1L));
        assertEquals("Post office with id=1 not found.", exception.getMessage(),
                "The exception message does not match the expected one");
    }

    @Test
    protected void test_confirmationOfArrival_whenNotFoundRecordsAboutDispatch_shouldThrowException() {
        Mockito.when(officeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(generator.nextObject(PostOffice.class)));
        Mockito.when(dispatchRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(generator.nextObject(PostalDispatch.class)));
        Mockito.when(historyRepository.lastTransfer(Mockito.anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postalService.confirmationOfArrival(124L, 53L));
        assertEquals("The postal dispatch is not on the way", exception.getMessage(),
                "The exception message does not match the expected one");
    }

    @Test
    protected void test_confirmationOfReceipt_whenDispatchDelivered_shouldReturnDtoWithStatusReceived() {
        PostalDispatch postalDispatch = generator.nextObject(PostalDispatch.class);
        postalDispatch.getDestination().setId(1L);
        PostalDispatchHistory history = generator.nextObject(PostalDispatchHistory.class);
        history.getCurrentDestination().setId(1L);
        history.setStatus(PostalDispatchStatus.DELIVERED);
        PostalDispatchHistory newHistory = generator.nextObject(PostalDispatchHistory.class);
        newHistory.setStatus(PostalDispatchStatus.RECEIVED);
        newHistory.getCurrentDestination().setId(1L);
        Mockito.when(dispatchRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(postalDispatch));
        Mockito.when(historyRepository.lastTransfer(Mockito.anyLong())).thenReturn(Optional.of(history));
        Mockito.when(dispatchRepository.save(postalDispatch)).thenReturn(postalDispatch);
        Mockito.when(historyRepository.save(Mockito.any())).thenReturn(newHistory);

        PostalDispatchStatusDto dto = postalService.confirmationOfReceipt(1L);

        assertEquals(PostalDispatchStatus.RECEIVED, dto.getStatus());
    }
}