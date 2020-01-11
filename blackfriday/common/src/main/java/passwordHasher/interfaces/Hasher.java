package passwordHasher.interfaces;

import org.mindrot.jbcrypt.BCrypt;

public interface Hasher {

    static String hash(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    static String hash(String password, String salt){
        return BCrypt.hashpw(password, salt);
    }

    static boolean validate(String hashOne, String hashTwo){
        return hashOne.equals(hashTwo);
    }

    static String getSalt(String hash){
        return hash.substring(0, 29);
    }

}
