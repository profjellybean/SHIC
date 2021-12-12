package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.UnitOfQuantity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ItemStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long storageId;
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


    public ItemStorage(){}

    public ItemStorage(long id, String name, String notes, byte[] image, Date expDate, int amount, Location locationTag, UnitOfQuantity quantity) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.image = image;
        this.expDate = expDate;
        this.amount = amount;
        this.locationTag = locationTag;
        this.quantity = quantity;
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
}
