package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class ItemDto {
    private Long id;
    private String name;
    private UnitOfQuantityDto quantity;
    private Long groupId;

    public ItemDto(Long id) {
        this.id = id;
    }

    public ItemDto() {

    }

    public ItemDto(Long id, String name, UnitOfQuantityDto quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public ItemDto(Long id, String name, UnitOfQuantityDto quantity, Long groupId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.groupId = groupId;
    }

    public ItemDto(String name, UnitOfQuantityDto quantity) {
        this.name = name;
        this.quantity = quantity;
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

    public UnitOfQuantityDto getQuantity() {
        return quantity;
    }

    public void setQuantity(UnitOfQuantityDto quantity) {
        this.quantity = quantity;
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
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(id, itemDto.id) && Objects.equals(name, itemDto.name)
            && Objects.equals(quantity, itemDto.quantity) && Objects.equals(groupId, itemDto.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity, groupId);
    }

    @Override
    public String toString() {
        return "ItemDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", quantity=" + quantity
            + ", groupId=" + groupId
            + '}';
    }
}
