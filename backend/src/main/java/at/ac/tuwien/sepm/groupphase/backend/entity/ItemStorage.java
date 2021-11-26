package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Embeddable;

@Embeddable
public class ItemStorage {
    private Item item;

    public ItemStorage(){}

    public ItemStorage(Item item){
        this.item = item;
    }
}
