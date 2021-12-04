package at.ac.tuwien.sepm.groupphase.backend.entity;



import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.UnitOfQuantity;

import javax.persistence.*;


@Entity
@Table(name = "Item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Quantity")
    private UnitOfQuantity quantity;

    public Item(Long id) {
        this.id = id;
    }

    public Item() {

    }

    public Item(Long id, String name, UnitOfQuantity quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnitOfQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(UnitOfQuantity quantity) {
        this.quantity = quantity;
    }

}
