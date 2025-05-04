import java.util.HashMap;
import java.util.Map;


class StudentTelekomunikaci extends Student {
    private static final long serialVersionUID = 1L;
    private static final Map<Character, String> morseovka = new HashMap<>();

    static {
        morseovka.put('A', ".-");
        morseovka.put('B', "-...");
        morseovka.put('C', "-.-.");
        morseovka.put('D', "-..");
        morseovka.put('E', ".");
        morseovka.put('F', "..-.");
        morseovka.put('G', "--.");
        morseovka.put('H', "....");
        morseovka.put('I', "..");
        morseovka.put('J', ".---");
        morseovka.put('K', "-.-");
        morseovka.put('L', ".-..");
        morseovka.put('M', "--");
        morseovka.put('N', "-.");
        morseovka.put('O', "---");
        morseovka.put('P', ".--.");
        morseovka.put('Q', "--.-");
        morseovka.put('R', ".-.");
        morseovka.put('S', "...");
        morseovka.put('T', "-");
        morseovka.put('U', "..-");
        morseovka.put('V', "...-");
        morseovka.put('W', ".--");
        morseovka.put('X', "-..-");
        morseovka.put('Y', "-.--");
        morseovka.put('Z', "--..");
        morseovka.put(' ', "/");
    }

    public StudentTelekomunikaci(int id, String jmeno, String prijmeni, int rokNarozeni) {
        super(id, jmeno, prijmeni, rokNarozeni);
    }

    public void provedSpecialniDovednost() {
        String celeJmeno = (getJmeno() + " " + getPrijmeni()).toUpperCase();
        System.out.println("Morseovka pro " + getJmeno() + " " + getPrijmeni() + ":");
        
        for (char c : celeJmeno.toCharArray()) {
            String morse = morseovka.getOrDefault(c, "?");
            System.out.print(morse + " ");
        }
        System.out.println();
    }
}
