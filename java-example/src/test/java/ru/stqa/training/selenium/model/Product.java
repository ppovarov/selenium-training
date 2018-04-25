package ru.stqa.training.selenium.model;

import java.awt.*;

public class Product {
    private String name;

    private String price;
    private Color priceColor;
    private String priceStyle;
    private String priceSize;

    private String discountPrice;
    private Color discountPriceColor;
    private String discountPriceStyle;
    private String discountPriceSize;

    private String link;


    public String getName() {
        return name;
    }


    public String getPrice() {
        return price;
    }

    public Color getPriceColor() {
        return priceColor;
    }

    public String getPriceStyle() {
        return priceStyle;
    }

    public String getPriceSize() {
        return priceSize;
    }


    public String getDiscountPrice() {
        return discountPrice;
    }

    public Color getDiscountPriceColor() {
        return discountPriceColor;
    }

    public String getDiscountPriceStyle() {
        return discountPriceStyle;
    }

    public String getDiscountPriceSize() {
        return discountPriceSize;
    }


    public String getLink() {
        return link;
    }


    public Product withName(String name) {
        this.name = name;
        return this;
    }


    public Product withPrice(String price) {
        this.price = price;
        return this;
    }

    public Product withPriceColor(Color priceColor) {
        this.priceColor = priceColor;
        return this;
    }

    public Product withPriceStyle(String priceStyle) {
        this.priceStyle = priceStyle;
        return this;
    }

    public Product withPriceSize(String priceSize) {
        this.priceSize = priceSize;
        return this;
    }


    public Product withDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
        return this;
    }

    public Product withDiscountPriceColor(Color discountPriceColor) {
        this.discountPriceColor = discountPriceColor;
        return this;
    }

    public Product withDiscountPriceStyle(String discountPriceStyle) {
        this.discountPriceStyle = discountPriceStyle;
        return this;
    }

    public Product withDiscountPriceSize(String discountPriceSize) {
        this.discountPriceSize = discountPriceSize;
        return this;
    }

    public Product withLink(String link) {
        this.link = link;
        return this;
    }

}
