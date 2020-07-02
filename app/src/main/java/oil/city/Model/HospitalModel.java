package oil.city.Model;

public class HospitalModel {
    private String Name, Adress, Time, Image, Phone;

    public HospitalModel() {
    }

    public HospitalModel(String name, String adress, String time, String phone, String image) {
        Name = name;
        Adress = adress;
        Time = time;
        Phone = phone;
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
