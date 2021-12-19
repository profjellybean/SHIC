package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class IdStringCollectionDto {

    private Long id;

    private Long additionalId;

    private String additionalString;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdditionalId() {
        return additionalId;
    }

    public void setAdditionalId(Long additionalId) {
        this.additionalId = additionalId;
    }

    public String getAdditionalString() {
        return additionalString;
    }

    public void setAdditionalString(String additionalString) {
        this.additionalString = additionalString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdStringCollectionDto that = (IdStringCollectionDto) o;
        return Objects.equals(id, that.id)
            && Objects.equals(additionalId, that.additionalId)
            && Objects.equals(additionalString, that.additionalString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, additionalId, additionalString);
    }

    @Override
    public String toString() {
        return "IdStringCollectionDto{"
            +
            "id=" + id
            +
            ", additionalId=" + additionalId
            +
            ", additionalString='" + additionalString + '\''
            +
            '}';
    }

    public static final class IdStringCollectionDtoBuilder {
        private Long id;
        private Long additionalId;
        private String additionalString;

        private IdStringCollectionDtoBuilder() {
        }

        public static IdStringCollectionDtoBuilder aRegisterDto() {
            return new IdStringCollectionDtoBuilder();
        }

        public IdStringCollectionDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public IdStringCollectionDtoBuilder withAdditionalId(Long additionalId) {
            this.additionalId = additionalId;
            return this;
        }

        public IdStringCollectionDtoBuilder withAdditionalString(String additionalString) {
            this.additionalString = additionalString;
            return this;
        }

        public IdStringCollectionDto build() {
            IdStringCollectionDto idStringCollectionDto = new IdStringCollectionDto();

            return idStringCollectionDto;
        }
    }
}

