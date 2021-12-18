package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotNull;

public class UnitOfQuantityDto {
    private Long id;
    @NotNull
    private String name;

    public UnitOfQuantityDto() {

    }

    public UnitOfQuantityDto(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
