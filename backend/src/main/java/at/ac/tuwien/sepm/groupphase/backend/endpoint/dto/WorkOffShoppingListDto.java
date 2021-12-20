package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;

import java.util.List;
import java.util.Objects;

public class WorkOffShoppingListDto {
    Long storageId;
    List<ItemStorage> boughtItems;

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public List<ItemStorage> getBoughtItems() {
        return boughtItems;
    }

    public void setBoughtItems(List<ItemStorage> boughtItems) {
        this.boughtItems = boughtItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkOffShoppingListDto that = (WorkOffShoppingListDto) o;
        return Objects.equals(storageId, that.storageId)
            && Objects.equals(boughtItems, that.boughtItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storageId, boughtItems);
    }

    @Override
    public String toString() {
        return "WorkOffShoppingListDto{"
            +
            "storageId=" + storageId
            +
            ", boughtItems=" + boughtItems
            +
            '}';
    }
}
