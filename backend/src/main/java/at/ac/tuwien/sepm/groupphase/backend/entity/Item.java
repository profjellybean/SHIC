package at.ac.tuwien.sepm.groupphase.backend.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "Item")
public class Item {
    @Id
    @Column(name = "Id")
    private int id;

    @Column(name = "Notes")
    private String notes;

    @Column(name = "Image")
    private byte[] image;

    @Column(name = "ExpDate")
    private Date expDate;

    @Column(name = "Amount")
    private int amount;

    public int getId() {
        return id;
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
