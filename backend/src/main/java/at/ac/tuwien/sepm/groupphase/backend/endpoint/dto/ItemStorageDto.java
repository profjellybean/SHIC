package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;

public class ItemStorageDto {
    @NotNull
    private Long storageId;
    private Long shoppingListId;
    private Long id;
    @NotNull
    private String name;
    private String quantity;
    private String notes;
    private byte[] image;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expDate;
    private int amount;
    private String locationTag;


    public ItemStorageDto() {
    }

    public ItemStorageDto(long storageId, String name) {
        this.storageId = storageId;
        this.name = name;
    }


    public ItemStorageDto(Long storageId, Long shoppingListId, Long id, String name, String quantity, String notes, byte[] image, Date expDate, int amount, String locationTag) {
        this.storageId = storageId;
        this.shoppingListId = shoppingListId;
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.notes = notes;
        this.image = image;
        this.expDate = expDate;
        this.amount = amount;
        this.locationTag = locationTag;
    }

    public ItemStorageDto(Long storageId, String name, String notes, int amount, String locationTag) {
        this.storageId = storageId;
        this.name = name;
        this.notes = notes;
        this.amount = amount;
        this.locationTag = locationTag;
    }

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

    public String getLocationTag() {
        return locationTag;
    }

    public void setLocationTag(String locationTag) {
        this.locationTag = locationTag;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "ItemStorageDto{"
            +
            "storageId=" + storageId
            +
            ", id=" + id
            +
            ", name='" + name + '\''
            +
            ", quantity=" + quantity
            +
            ", notes='" + notes + '\''
            +
            ", image=" + Arrays.toString(image)
            +
            ", expDate=" + expDate
            +
            ", amount=" + amount
            +
            ", locationTag=" + locationTag
            +
            '}';
    }
}
