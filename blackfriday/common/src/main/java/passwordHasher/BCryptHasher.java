package passwordHasher;

import org.mindrot.jbcrypt.BCrypt;

public interface BCryptHasher {

    static String hash(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    static String hash(String password, String salt){
        return BCrypt.hashpw(password, salt);
    }

    static String getSalt(String hash){
        return hash.substring(0, 29);
    }

}
