package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "trashOrUsed")
public class TrashOrUsed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date date;

    @Column
    private int amount;

    @Column
    private Long storageId;

    @OneToOne
    private UnitOfQuantity unitOfQuantity;
    @Column
    private String itemName;


    public TrashOrUsed(Long id, Date date, int amount, Long storageId, boolean trash) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.storageId = storageId;
    }

    public TrashOrUsed(Date date, int amount, Long storageId, boolean trash) {
        this.date = date;
        this.amount = amount;
        this.storageId = storageId;
    }

    public TrashOrUsed() {

    }

    public TrashOrUsed(Date date, int amount, Long storageId, UnitOfQuantity unitOfQuantity, String itemName) {
        this.date = date;
        this.amount = amount;
        this.storageId = storageId;
        this.unitOfQuantity = unitOfQuantity;
        this.itemName = itemName;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public TrashOrUsed(Long id, Date date, int amount, boolean trash) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    public TrashOrUsed(Long id) {
        this.id = id;
    }

    public TrashOrUsed(Date date, int amount, boolean trash) {
        this.date = date;
        this.amount = amount;
    }

    public UnitOfQuantity getUnitOfQuantity() {
        return unitOfQuantity;
    }

    public void setUnitOfQuantity(UnitOfQuantity unitOfQuantity) {
        this.unitOfQuantity = unitOfQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
