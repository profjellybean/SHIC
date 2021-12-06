package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.UnitOfQuantity;

import java.util.Arrays;
import java.util.Date;

public class ItemStorageDto{
    private Long storageId;
    private Long shoppingListId;
    private Long id;
    private String name;
    private UnitOfQuantity quantity;
    private String notes;
    private byte[] image;
    private Date expDate;
    private int amount;
    private Location locationTag;


    public ItemStorageDto(){}

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Location getLocationTag() {
        return locationTag;
    }

    public void setLocationTag(Location locationTag) {
        this.locationTag = locationTag;
    }

    public UnitOfQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(UnitOfQuantity quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "ItemStorageDto{" +
            "storageId=" + storageId +
            "shoppingListId=" + shoppingListId +
            ", id=" + id +
            ", name='" + name + '\'' +
            ", quantity=" + quantity +
            ", notes='" + notes + '\'' +
            ", image=" + Arrays.toString(image) +
            ", expDate=" + expDate +
            ", amount=" + amount +
            ", locationTag=" + locationTag +
            '}';
    }
}
