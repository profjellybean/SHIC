package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Entity
public class ItemStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String notes;
    @Column
    private byte[] image;
    @Column
    private Date expDate;
    @Column
    private int amount;
    @Column
    private String locationTag;
    @OneToOne
    private UnitOfQuantity unitOfQuantity;
    @Column
    private Long storageId;
    @Column
    private Long shoppingListId;


    public ItemStorage() {
    }

    public ItemStorage(Long id) {
        this.id = id;
    }

    public ItemStorage(String name, String notes, byte[] image, Date expDate, int amount, String locationTag,  UnitOfQuantity unitOfQuantity, Long storageId, Long shoppingListId) {
        this.name = name;
        this.notes = notes;
        this.image = image;
        this.expDate = expDate;
        this.amount = amount;
        this.locationTag = locationTag;
        this.unitOfQuantity = unitOfQuantity;
        this.storageId = storageId;
        this.shoppingListId = shoppingListId;
    }

    public ItemStorage(ItemStorage itemStorage) {
        //this = itemStorage.clone();
        // this.id = itemStorage.getId();
        this.name = itemStorage.getName();
        this.notes = itemStorage.notes;
        this.image = itemStorage.image;
        this.expDate = itemStorage.expDate;
        this.amount = itemStorage.amount;
        this.locationTag = itemStorage.locationTag;
        this.unitOfQuantity = itemStorage.unitOfQuantity;
        this.storageId = itemStorage.storageId;
        this.shoppingListId = itemStorage.shoppingListId;
    }

    public ItemStorage(long storageId, String name) {
        this.storageId = storageId;
        this.name = name;
    }


    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
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

    public UnitOfQuantity getQuantity() {
        return unitOfQuantity;
    }

    public void setQuantity(UnitOfQuantity unitOfQuantity) {
        this.unitOfQuantity = unitOfQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemStorage that = (ItemStorage) o;
        return Objects.equals(name, that.name)
            && unitOfQuantity == that.unitOfQuantity
            && amount == that.amount && Objects.equals(id, that.id)
            && Objects.equals(notes, that.notes) && Arrays.equals(image, that.image)
            && Objects.equals(expDate, that.expDate) && Objects.equals(locationTag, that.locationTag)
            && Objects.equals(shoppingListId, that.shoppingListId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, notes, expDate, amount, locationTag, unitOfQuantity, storageId);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return "ItemStorage{"
            +
            "id=" + id
            +
            ", name='" + name + '\''
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
            ", unitOfQuantity=" + unitOfQuantity
            +
            ", storageId=" + storageId
            +
            ", shoppingListId=" + shoppingListId
            +
            '}';
    }
}
