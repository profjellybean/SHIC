package at.ac.tuwien.sepm.groupphase.backend.entity;


import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.UnitOfQuantity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "Item")
public class Item {

    public Item() {}
    public Item(Long id) { this.id = id; }

    @Id
    @Column(name = "Id")
    private Long id;

    @Column(name = "Notes")
    private String notes;

    @Column(name = "Image")
    private byte[] image;

    @Column(name = "ExpDate")
    private Date expDate;

    @Column(name = "Amount")
    private int amount;

    @Column(name = "LocationTag")
    private Location locationTag;

    @Column(name = "Quantity")
    private UnitOfQuantity quantity;



    public Long getId() {
        return id;
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
}
