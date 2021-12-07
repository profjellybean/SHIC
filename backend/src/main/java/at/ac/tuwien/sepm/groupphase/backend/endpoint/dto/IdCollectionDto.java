package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class IdCollectionDto {

    private Long id;

    private Long firstAdditionalId;

    private Long secondAdditionalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFirstAdditionalId() {
        return firstAdditionalId;
    }

    public void setFirstAdditionalId(Long firstAdditionalId) {
        this.firstAdditionalId = firstAdditionalId;
    }

    public Long getSecondAdditionalId() {
        return secondAdditionalId;
    }

    public void setSecondAdditionalId(Long secondAdditionalId) {
        this.secondAdditionalId = secondAdditionalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdCollectionDto that = (IdCollectionDto) o;
        return Objects.equals(id, that.id)
            && Objects.equals(firstAdditionalId, that.firstAdditionalId)
            && Objects.equals(secondAdditionalId, that.secondAdditionalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstAdditionalId, secondAdditionalId);
    }

    @Override
    public String toString() {
        return "IdCollectionDto{" +
            "id=" + id +
            ", firstAdditionalId=" + firstAdditionalId +
            ", secondAdditionalId=" + secondAdditionalId +
            '}';
    }

    public static final class IdCollectionDtoBuilder {
        private Long id;
        private Long firstAdditionalId;
        private Long secondAdditionalId;

        private IdCollectionDtoBuilder() {
        }

        public static IdCollectionDtoBuilder aRegisterDto() {
            return new IdCollectionDtoBuilder();
        }

        public IdCollectionDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public IdCollectionDtoBuilder withBill(Long firstAdditionalId) {
            this.firstAdditionalId = firstAdditionalId;
            return this;
        }

        public IdCollectionDtoBuilder withUser(Long secondAdditionalId) {
            this.secondAdditionalId = secondAdditionalId;
            return this;
        }

        public IdCollectionDto build() {
            IdCollectionDto idCollectionDto = new IdCollectionDto();
            idCollectionDto.setId(id);
            idCollectionDto.setFirstAdditionalId(firstAdditionalId);
            idCollectionDto.setSecondAdditionalId(secondAdditionalId);
            return idCollectionDto;
        }
    }
}

