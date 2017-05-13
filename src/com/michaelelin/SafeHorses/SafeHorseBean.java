package com.michaelelin.SafeHorses;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "safehorses")
public class SafeHorseBean {

    @Id
    private int id;

    @NotNull
    private String owner;
    private String name;

    private int variant;
    private int color;
    private int style;
    private int age;
    private int speed;
    private int jump;

    // Serialized as JSON
    private String inventory;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVariant() {
        return this.variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getStyle() {
        return this.style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public int getJump() {
        return jump;
    }
    
    public void setJump(int jump) {
        this.jump = jump;
    }

    public String getInventory() {
        return this.inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

}
