package com.OnlineElectronicsStore.OnlineElectronicsStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Категорія товарів")
public class CategoryDto {

    @Schema(
            description = "ID категорії",
            example = "5"
    )
    private Long id;

    @Schema(
            description = "Назва категорії",
            example = "Електроніка"
    )
    private String name;

    public CategoryDto() {}

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // ====== Геттери та сеттери ======

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
