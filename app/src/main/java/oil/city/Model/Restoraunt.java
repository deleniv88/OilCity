package oil.city.Model;

public class Restoraunt {

    private String Name, Image, TextOfRest, PhoneOfRest, Img, Img1, Location, Adress, Time, Instagram;

    public Restoraunt() {
    }

    public Restoraunt(String instagram, String name, String image, String textOfRest, String phoneOfRest, String img, String img1, String location, String adress, String time) {
        Instagram = instagram;
        Name = name;
        Image = image;
        TextOfRest = textOfRest;
        PhoneOfRest = phoneOfRest;
        Img = img;
        Img1 = img1;
        Location = location;
        Adress = adress;
        Time = time;
    }

    public String getInstagram() {
        return Instagram;
    }

    public void setInstagram(String instagram) {
        Instagram = instagram;
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

    public String getTextOfRest() {
        return TextOfRest;
    }

    public void setTextOfRest(String textOfRest) {
        TextOfRest = textOfRest;
    }

    public String getPhoneOfRest() {
        return PhoneOfRest;
    }

    public void setPhoneOfRest(String phoneOfRest) {
        PhoneOfRest = phoneOfRest;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getImg1() {
        return Img1;
    }

    public void setImg1(String img1) {
        Img1 = img1;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

}