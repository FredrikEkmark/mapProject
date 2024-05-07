package com.fredrik.mapProject.gameRunDomain.model.event;

import jakarta.persistence.Column;

public class EventManaCost {

    private int manpower = 0;
    private int food = 0;
    private int stone = 0;
    private int wood = 0;
    private int leather = 0;
    private int furniture = 0;
    private int simpleClothes = 0;

    public EventManaCost() {
    }

    public void setManpower(int manpower) {
        this.manpower = manpower;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public void setLeather(int leather) {
        this.leather = leather;
    }

    public void setFurniture(int furniture) {
        this.furniture = furniture;
    }

    public void setSimpleClothes(int simpleClothes) {
        this.simpleClothes = simpleClothes;
    }

    public int getManpower() {
        return manpower;
    }

    public int getFood() {
        return food;
    }

    public int getStone() {
        return stone;
    }

    public int getWood() {
        return wood;
    }

    public int getLeather() {
        return leather;
    }

    public int getFurniture() {
        return furniture;
    }

    public int getSimpleClothes() {
        return simpleClothes;
    }

    public int payFood() {
        int foodPay = food;
        food = 0;
        return foodPay;
    }

    public int payStone() {
        int stonePay = stone;
        stone = 0;
        return stonePay;
    }

    public int payWood() {
        int woodPay = wood;
        wood = 0;
        return woodPay;
    }

    public int payLeather() {
        int leatherPay = leather;
        leather = 0;
        return leatherPay;
    }

    public int payFurniture() {
        int furniturePay = furniture;
        furniture = 0;
        return furniturePay;
    }

    public int paySimpleClothes() {
        int simpleClothesPay = simpleClothes;
        simpleClothes = 0;
        return simpleClothesPay;
    }
}

