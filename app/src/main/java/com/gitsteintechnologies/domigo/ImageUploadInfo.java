package com.gitsteintechnologies.domigo;

/**
 * Created by Aditya on 5/1/2018.
 */

public class ImageUploadInfo {

    public String imageName;
    public String imageurl;
    public String name;
    public String username;
    public String password;
    public String mobile;



    public ImageUploadInfo() {

    }

    public String getImageName() {
        return imageName;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }





    public ImageUploadInfo(String Username, String Name, String Password, String mobile, String imagename, String url) {

        this.imageName = imagename;
        this.imageurl= url;
        this.name = Name;
        this.username = Username;
        this.password = Password;
        this.mobile = mobile;



    }


}