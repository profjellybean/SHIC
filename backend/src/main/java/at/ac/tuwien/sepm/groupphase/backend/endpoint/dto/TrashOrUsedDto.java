package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class TrashOrUsedDto {

    private Long id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private int amount;
    @NotNull
    private Long storageId;
    private UnitOfQuantity unitOfQuantity;
    private String itemName;


    public TrashOrUsedDto(Date date, int amount, Long storageId, UnitOfQuantity unitOfQuantity, String itemName) {
        this.date = date;
        this.amount = amount;
        this.storageId = storageId;
        this.unitOfQuantity = unitOfQuantity;
        this.itemName = itemName;
    }


    public TrashOrUsedDto() {
    }

    public TrashOrUsedDto(Date date, int amount, Long storageId) {
        this.date = date;
        this.amount = amount;
        this.storageId = storageId;
    }

    public TrashOrUsedDto(Long id, Date date, int amount, Long storageId) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.storageId = storageId;
    }

    public String getItemName() {
        return itemName;
    }

    public UnitOfQuantity getUnitOfQuantity() {
        return unitOfQuantity;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setUnitOfQuantity(UnitOfQuantity unitOfQuantity) {
        this.unitOfQuantity = unitOfQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }
}
