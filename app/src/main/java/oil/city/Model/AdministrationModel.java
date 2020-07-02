package oil.city.Model;

public class AdministrationModel {
    private String Name, Adress, Time, Image, Phone, Location;

    public AdministrationModel() {
    }

    public AdministrationModel(String name, String adress, String time, String image, String phone, String location) {
        Name = name;
        Adress = adress;
        Time = time;
        Image = image;
        Phone = phone;
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
