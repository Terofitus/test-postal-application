package com.example.testpostalapplication.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PageableCreator {
    public Pageable toPageable(Integer from, Integer size, Sort sort) {
        if (sort == null) sort = Sort.unsorted();
        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("The from argument cannot be less than size and 0, the " +
                    "size argument cannot be equal to or less than 0.");
        }
        return PageRequest.of(from / size, size, sort);
    }
}
