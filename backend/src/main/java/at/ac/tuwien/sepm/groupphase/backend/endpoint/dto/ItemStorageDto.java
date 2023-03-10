package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class ItemStorageDto {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @NotNull
    private Long storageId;
    private Long shoppingListId;
    private Long id;
    @NotNull
    private String name;
    private UnitOfQuantityDto unitOfQuantityDto;
    private String notes;
    private byte[] image;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expDate;
    private int amount;
    private String locationTag;


    public ItemStorageDto() {
    }

    public ItemStorageDto(Long storageId, String name) {
        this.storageId = storageId;
        this.name = name;
    }

    public ItemStorageDto(String name, Long shoppingListId) {
        this.shoppingListId = shoppingListId;
        this.name = name;
    }


    public ItemStorageDto(Long storageId, Long shoppingListId, Long id, String name, UnitOfQuantityDto unitOfQuantityDto, String notes, byte[] image, Date expDate, int amount, String locationTag) {
        this.storageId = storageId;
        this.shoppingListId = shoppingListId;
        this.id = id;
        this.name = name;
        this.unitOfQuantityDto = unitOfQuantityDto;
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

    public UnitOfQuantityDto getQuantity() {
        return unitOfQuantityDto;
    }

    public void setQuantity(UnitOfQuantityDto unitOfQuantityDto) {
        this.unitOfQuantityDto = unitOfQuantityDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemStorageDto that = (ItemStorageDto) o;
        return amount == that.amount
            && Objects.equals(storageId, that.storageId)
            && Objects.equals(shoppingListId, that.shoppingListId)
            && Objects.equals(id, that.id)
            && Objects.equals(name, that.name)
            && Objects.equals(unitOfQuantityDto, that.unitOfQuantityDto)
            && Objects.equals(notes, that.notes)
            && Arrays.equals(image, that.image)
            && Objects.equals(expDate, that.expDate)
            && Objects.equals(locationTag, that.locationTag);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(storageId, shoppingListId, id, name, unitOfQuantityDto, notes, expDate, amount, locationTag);
        result = 31 * result + Arrays.hashCode(image);
        return result;
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
            ", unitOfQuantity=" + unitOfQuantityDto
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
            ", shoppingListId=" + shoppingListId
            +
            '}';
    }
}
