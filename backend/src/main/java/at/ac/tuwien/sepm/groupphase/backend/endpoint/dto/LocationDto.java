package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class LocationDto {
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Long storageId;

    public LocationDto() {

    }

    public LocationDto(String name, Long storageId) {
        this.name = name;
        this.storageId = storageId;
    }

    public LocationDto(Long id, String name, Long storageId) {
        this.id = id;
        this.name = name;
        this.storageId = storageId;
    }

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

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationDto that = (LocationDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(storageId, that.storageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, storageId);
    }

    @Override
    public String toString() {
        return "LocationDto{"
            + "id="
            + id
            + ", name='"
            + name
            + '\''
            + ", storageId="
            + storageId
            + '}';
    }
}
