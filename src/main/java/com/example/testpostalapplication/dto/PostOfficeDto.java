package com.example.testpostalapplication.dto;

import com.example.testpostalapplication.model.PostOffice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostOfficeDto {
    @Schema(description = "Название почтового отделения")
    private String name;
    @Schema(description = "Адрес почтового отделения")
    private String address;
    @Schema(description = "Индекс почтового отделения")
    private String index;

    public PostOfficeDto(PostOffice postOffice) {
        name = postOffice.getName();
        address = postOffice.getAddress();
        index = postOffice.getIndex();
    }
}
