package oil.city.Model;

public class SkiModel {

    private String Name, Image, TextOfRest, Instagram;

    public SkiModel() {
    }

    public SkiModel(String name, String image, String textOfRest,String instagram) {
        Name = name;
        Image = image;
        TextOfRest = textOfRest;
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

    public String getTextOfRest() {
        return TextOfRest;
    }

    public void setTextOfRest(String textOfRest) {
        TextOfRest = textOfRest;
    }
}
