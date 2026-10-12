package tests;

import models.Pet;
import org.testng.annotations.Test;
import testdata.PetTestDataBuilder;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class PetBuilderTest {

    @Test
    public void createPetUsingBuilder(){

        Pet pet = new PetTestDataBuilder()
                .withName("Testing Builder")
                .withStatus("pending")
                .build();

        assertNotNull(pet);
        assertTrue(pet.getId()>0);
        assertEquals("pending", pet.getStatus());
        assertNotNull(pet.getPhotoUrls());
        assertNotNull(pet.getTags());
    }
}
