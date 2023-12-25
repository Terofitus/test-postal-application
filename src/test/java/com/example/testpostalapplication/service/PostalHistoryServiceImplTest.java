package com.example.testpostalapplication.service;

import com.example.testpostalapplication.dto.PostalDispatchHistoryDto;
import com.example.testpostalapplication.exception.ObjectNotFoundException;
import com.example.testpostalapplication.model.PostalDispatch;
import com.example.testpostalapplication.model.PostalDispatchHistory;
import com.example.testpostalapplication.repository.DispatchHistoryRepository;
import com.example.testpostalapplication.repository.DispatchRepository;
import com.example.testpostalapplication.util.PageableCreator;
import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class PostalHistoryServiceImplTest {
    private final EasyRandom generator = new EasyRandom();
    @InjectMocks
    private PostalHistoryServiceImpl historyService;
    @Mock
    private DispatchHistoryRepository historyRepository;
    @Mock
    private DispatchRepository dispatchRepository;


    @Test
    protected void test_getStatusOfDispatch_whenHistoryNotFound_shouldThrowException() {
        Mockito.when(dispatchRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(
                generator.nextObject(PostalDispatch.class)));
        Mockito.when(historyRepository.lastTransfer(Mockito.anyLong())).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> historyService.getStatusOfDispatch(1L));
        assertEquals("The postal dispatch history not found", exception.getMessage());
    }

    @Test
    protected void test_getHistoryOfDispatch_whenHistoryFound_shouldReturnSortedByTimeList() {
        List<PostalDispatchHistory> histories = generator.objects(PostalDispatchHistory.class, 20)
                .sorted(Comparator.comparing(PostalDispatchHistory::getDateTime)).toList();
        Mockito.when(historyRepository.findAllByPostalDispatchIdOrderByDateTime(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new PageImpl<>(histories));

        List<PostalDispatchHistoryDto> dtoList = historyService.getHistoryOfDispatch(1L,
                PageableCreator.toPageable(0, 20, null));

        assertTrue(dtoList.get(0).getDateTime().isBefore((dtoList.get(19).getDateTime())));
    }
}