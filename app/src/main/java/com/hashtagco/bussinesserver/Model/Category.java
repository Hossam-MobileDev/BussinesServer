package com.hashtagco.bussinesserver.Model;

public class Category {

    private String Name;
    private String Image;
    private String MenuId;
    public Category(){

    }

    public Category(String name) {
        Name = name;

    }
    public Category(String name, String image, String menuId) {
        Name = name;
        Image = image;
        MenuId = menuId;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
