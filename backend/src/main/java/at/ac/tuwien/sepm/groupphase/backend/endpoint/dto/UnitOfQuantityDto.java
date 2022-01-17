package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UnitOfQuantityDto {
    private Long id;
    @NotNull
    private String name;

    private Long groupId;

    public UnitOfQuantityDto() {

    }

    public UnitOfQuantityDto(String name, Long groupId) {
        this.name = name;
        this.groupId = groupId;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnitOfQuantityDto that = (UnitOfQuantityDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "UnitOfQuantityDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + '}';
    }
}
