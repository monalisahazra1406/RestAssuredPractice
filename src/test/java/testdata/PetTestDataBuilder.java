package testdata;

import models.Category;
import models.Pet;
import models.Tag;

import java.util.List;

public class PetTestDataBuilder {

    private long id = System.currentTimeMillis();
    private Category category = new Category(1,"cats");
    private String name = "PetTestDataBuilderCat";
    private List<String> photoUrls = List.of("https://example.com/default-dog.jpg");
    private List<Tag> tags = List.of();
    private String status = "available";

    public PetTestDataBuilder withId(long id){
        this.id = id;
        return this;
    }

    public PetTestDataBuilder withName(String name){
        this .name = name;
        return this;
    }

    public PetTestDataBuilder withCategory(Category category){
        this.category = category;
        return this;
    }

    public PetTestDataBuilder withPhotoUrls(List<String> photoUrls){
        this.photoUrls = photoUrls;
        return this;
    }

    public PetTestDataBuilder withTags(List<Tag> tags){
        this.tags = tags;
        return this;
    }

    public PetTestDataBuilder withStatus(String status){
        this.status = status;
        return this;
    }

    public Pet build(){
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setCategory(category);
        pet.setPhotoUrls(photoUrls);
        pet.setTags(tags);
        pet.setStatus(status);

        return pet;
    }

}
