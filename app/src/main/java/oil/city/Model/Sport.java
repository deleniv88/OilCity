package oil.city.Model;

public class Sport {
    private String Name, Image, TextOfSport, Img, Img1, Img2, Img3, Img4, Img5, Location, Adress, Time, Phone, Instagram;

    public Sport() {
    }

    public Sport(String instagram, String name, String image, String textOfSport, String img, String img1, String img2, String img3, String img4, String img5, String location, String adress, String time, String phone) {
        Name = name;
        Image = image;
        TextOfSport = textOfSport;
        Img = img;
        Img1 = img1;
        Img2 = img2;
        Img3 = img3;
        Img4 = img4;
        Img5 = img5;
        Location = location;
        Adress = adress;
        Time = time;
        Phone = phone;
        Instagram = instagram;
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

    public String getTextOfSport() {
        return TextOfSport;
    }

    public void setTextOfSport(String textOfSport) {
        TextOfSport = textOfSport;
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

    public String getImg2() {
        return Img2;
    }

    public void setImg2(String img2) {
        Img2 = img2;
    }

    public String getImg3() {
        return Img3;
    }

    public void setImg3(String img3) {
        Img3 = img3;
    }

    public String getImg4() {
        return Img4;
    }

    public void setImg4(String img4) {
        Img4 = img4;
    }

    public String getImg5() {
        return Img5;
    }

    public void setImg5(String img5) {
        Img5 = img5;
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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
