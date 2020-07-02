package oil.city.Model;

public class Sport {
    private String Name, Image, TextOfSport;

    public Sport() {
    }

    public Sport(String name, String image, String textOfSport) {
        Name = name;
        Image = image;
        TextOfSport = textOfSport;
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
}
