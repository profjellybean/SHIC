package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.UnitOfQuantity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;

@Entity
public class ItemStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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
    private Location locationTag;
    @Column
    private UnitOfQuantity quantity;
    @Column
    private Long storageId;


    public ItemStorage(){}

    public ItemStorage(String name, String notes, byte[] image, Date expDate, int amount, Location locationTag, UnitOfQuantity quantity, Long storageId) {
        this.name = name;
        this.notes = notes;
        this.image = image;
        this.expDate = expDate;
        this.amount = amount;
        this.locationTag = locationTag;
        this.quantity = quantity;
        this.storageId = storageId;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        return "ItemStorage{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", notes='" + notes + '\'' +
            ", image=" + Arrays.toString(image) +
            ", expDate=" + expDate +
            ", amount=" + amount +
            ", locationTag=" + locationTag +
            ", quantity=" + quantity +
            ", storageId=" + storageId +
            '}';
    }
}
