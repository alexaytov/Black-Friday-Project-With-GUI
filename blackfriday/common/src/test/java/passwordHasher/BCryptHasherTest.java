package passwordHasher;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BCryptHasherTest {

    private String toBeHashed = "test";
    private String salt = "$2a$10$b/dk./HeVs.IYzQ1I8Aliu";

    @Test
    public void hashMethodShouldReturnHashedString(){
        String hashedString = BCryptHasher.hash(toBeHashed);
        assertNotEquals(toBeHashed, hashedString);
    }

    @Test
    public void shouldUseSaltWhenHashing(){
        String hashedWithoutSalt = BCryptHasher.hash(this.toBeHashed);
        String hashedWithSalt = BCryptHasher.hash(this.toBeHashed, this.salt);

        assertNotEquals(hashedWithoutSalt, hashedWithSalt);
    }

    @Test
    public void shouldReturnCorrectUsedSalt(){
        String hashedString = BCryptHasher.hash(this.toBeHashed, this.salt);
        String usedSalt = BCryptHasher.getSalt(hashedString);

        assertEquals(this.salt, usedSalt);
    }

    @Test
    public void shouldReturnSameHashedWhenUsedSameStringAndSameSalt(){
        String firstHashedString = BCryptHasher.hash(this.toBeHashed, this.salt);
        String secondHashedString = BCryptHasher.hash(this.toBeHashed, this.salt);

        assertEquals(firstHashedString, secondHashedString);
    }
}
