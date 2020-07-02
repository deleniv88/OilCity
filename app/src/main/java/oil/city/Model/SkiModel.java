package oil.city.Model;

public class SkiModel {

    private String Name, Image, TextOfRest;

    public SkiModel() {
    }

    public SkiModel(String name, String image, String textOfRest) {
        Name = name;
        Image = image;
        TextOfRest = textOfRest;
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
}
