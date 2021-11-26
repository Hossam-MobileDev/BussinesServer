package com.hashtagco.bussinesserver.Model;

public class Banner
{
    private String image;
    private String name;
    private String newprice;

    public String getNewprice() {
        return newprice;
    }

    public void setNewprice(String newprice) {
        this.newprice = newprice;
    }



    public Banner() {
    }

    public Banner(String image, String name, String newprice) {
        this.image = image;
        this.name = name;
        this.newprice = newprice;
    }

  /*  public Banner(String image, String name, String id, String menuId) {
        this.image = image;
        this.name = name;
        this.id = id;
        this.menuId = menuId;
    }*/



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }







}
