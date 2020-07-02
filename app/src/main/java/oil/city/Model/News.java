package oil.city.Model;

public class News {
    private String Name, Image, Description, Img, Img1;

    public News() {
    }

    public News(String name, String image, String description, String img, String img1) {
        Name = name;
        Image = image;
        Description = description;
        Img = img;
        Img1 = img1;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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
}
