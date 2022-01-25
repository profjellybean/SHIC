package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trashOrUsedItem")
public class TrashOrUsedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private int amount;
    @Column
    private String itemName;
    @Column
    private Long storageId;

    public TrashOrUsedItem() {
    }

    public TrashOrUsedItem(int amount, String itemName, Long storageId) {
        this.amount = amount;
        this.itemName = itemName;
        this.storageId = storageId;
    }

    public TrashOrUsedItem(Long id, int amount, String itemName, Long storageId) {
        this.id = id;
        this.amount = amount;
        this.itemName = itemName;
        this.storageId = storageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

}
