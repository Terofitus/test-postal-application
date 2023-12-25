package com.example.testpostalapplication.service;

import com.example.testpostalapplication.dto.PostalDispatchCreateDto;
import com.example.testpostalapplication.dto.PostalDispatchGetDto;
import com.example.testpostalapplication.dto.PostalDispatchStatusDto;

public interface PostalService {
    PostalDispatchGetDto addNewDispatch(PostalDispatchCreateDto createDto);

    PostalDispatchStatusDto dispatchToPostOffice(Long dispatchId, Long postOfficeId);

    PostalDispatchStatusDto confirmationOfArrival(Long dispatchId, Long postOfficeId);

    PostalDispatchStatusDto confirmationOfReceipt(Long dispatchId);
}
