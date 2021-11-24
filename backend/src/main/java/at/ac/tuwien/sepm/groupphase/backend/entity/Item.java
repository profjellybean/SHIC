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


    private Date expDate;




}
