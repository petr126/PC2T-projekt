import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class StudentKyberBezpecnosti extends Student {
    private static final long serialVersionUID = 1L;

    public StudentKyberBezpecnosti(int id, String jmeno, String prijmeni, int rokNarozeni) {
        super(id, jmeno, prijmeni, rokNarozeni);
    }

    public void provedSpecialniDovednost() {
        try {
            String celeJmeno = getJmeno() + " " + getPrijmeni();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(celeJmeno.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            System.out.println("SHA-256 hash pro " + celeJmeno + ":");
            System.out.println(hexString.toString());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Chyba pri generovani hashe: " + e.getMessage());
        }
    }
}
