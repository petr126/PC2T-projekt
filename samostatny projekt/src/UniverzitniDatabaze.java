import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class UniverzitniDatabaze {
    private static final Scanner scanner = new Scanner(System.in);
    private static StudentDatabaze studentDatabaze;

    public static void main(String[] args) {
        // Inicializace pripojeni k databazi (upravte parametry podle potreby)
        String dbUrl = "jdbc:mysql://localhost:3306/univerzitni_db";
        String dbUzivatel = "root";
        String dbHeslo = "heslo";
        
        studentDatabaze = new StudentDatabazeImpl(dbUrl, dbUzivatel, dbHeslo);
        
        // Nacteni dat z databaze pri spusteni
        studentDatabaze.nactiVseZDatabaze();
        
        boolean bezi = true;
        while (bezi) {
            System.out.println("\nSystem univerzitni databaze studentu");
            System.out.println("1. Pridat noveho studenta");
            System.out.println("2. Pridat studentovi znamku");
            System.out.println("3. Odebrat studenta");
            System.out.println("4. Najit studenta podle ID");
            System.out.println("5. Prokazat studentovu dovednost");
            System.out.println("6. Zobrazit vsechny studenty (abecedne)");
            System.out.println("7. Zobrazit studenty telekomunikaci");
            System.out.println("8. Zobrazit studenty kyberneticke bezpecnosti");
            System.out.println("9. Zobrazit prumery podle oboru");
            System.out.println("10. Zobrazit pocty studentu podle oboru");
            System.out.println("11. Ulozit studenta do souboru");
            System.out.println("12. Nacist studenta ze souboru");
            System.out.println("0. Ukoncit");
            
            System.out.print("Zvolte moznost: ");
            int volba = scanner.nextInt();
            scanner.nextLine(); // Spotrebovat novy radek
            
            try {
                switch (volba) {
                    case 1 -> pridatNovehoStudenta();
                    case 2 -> pridatZnamkuStudentovi();
                    case 3 -> odebratStudenta();
                    case 4 -> najitStudenta();
                    case 5 -> provedStudentovuDovednost();
                    case 6 -> vypisVsechnyStudenty();
                    case 7 -> vypisStudentyTelekomunikace();
                    case 8 -> vypisStudentyKyberBezpecnosti();
                    case 9 -> vypisPrumeryOboru();
                    case 10 -> vypisPoctyStudentu();
                    case 11 -> ulozStudentaDoSouboru();
                    case 12 -> nactiStudentaZeSouboru();
                    case 0 -> {
                        bezi = false;
                        // Ulozeni dat do databaze pred ukoncenim
                        studentDatabaze.ulozVseDoDatabaze();
                        System.out.println("Data ulozena. Ukoncuji...");
                    }
                    default -> System.out.println("Neplatna volba. Zkuste to znovu.");
                }
            } catch (Exception e) {
                System.err.println("Chyba: " + e.getMessage());
            }
        }
    }

    private static void pridatNovehoStudenta() {
        System.out.println("\nPridat noveho studenta");
        System.out.println("1. Student telekomunikaci");
        System.out.println("2. Student kyberneticke bezpecnosti");
        System.out.print("Zvolte typ studenta: ");
        int typ = scanner.nextInt();
        scanner.nextLine(); // Spotrebovat novy radek
        
        if (typ != 1 && typ != 2) {
            System.out.println("Neplatny typ studenta");
            return;
        }
        
        System.out.print("Zadejte jmeno: ");
        String jmeno = scanner.nextLine();
        
        System.out.print("Zadejte prijmeni: ");
        String prijmeni = scanner.nextLine();
        
        System.out.print("Zadejte rok narozeni: ");
        int rokNarozeni = scanner.nextInt();
        scanner.nextLine(); // Spotrebovat novy radek
        
        Student student;
        StudentDatabazeImpl dbImpl = (StudentDatabazeImpl) studentDatabaze;
        int id = dbImpl.getDalsiId();
        
        if (typ == 1) {
            student = new StudentTelekomunikaci(id, jmeno, prijmeni, rokNarozeni);
        } else {
            student = new StudentKyberBezpecnosti(id, jmeno, prijmeni, rokNarozeni);
        }
        
        studentDatabaze.pridejStudenta(student);
        System.out.println("Student pridan s ID: " + id);
    }

    private static void pridatZnamkuStudentovi() {
        System.out.print("\nZadejte ID studenta: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Spotrebovat novy radek
        
        System.out.print("Zadejte znamku (1-5): ");
        int znamka = scanner.nextInt();
        scanner.nextLine(); // Spotrebovat novy radek
        
        studentDatabaze.pridejZnamkuStudentovi(id, znamka);
        System.out.println("Znamka uspesne pridana");
    }

    private static void odebratStudenta() {
        System.out.print("\nZadejte ID studenta k odebrani: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Spotrebovat novy radek
        
        studentDatabaze.odeberStudenta(id);
        System.out.println("Student uspesne odebran");
    }

    private static void najitStudenta() {
        System.out.print("\nZadejte ID studenta: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Spotrebovat novy radek
        
        Student student = studentDatabaze.najdiStudenta(id);
        if (student != null) {
            System.out.println("\nStudent nalezen:");
            System.out.println(student);
        } else {
            System.out.println("Student nenalezen");
        }
    }

    private static void provedStudentovuDovednost() {
        System.out.print("\nZadejte ID studenta: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Spotrebovat novy radek
        
        Student student = studentDatabaze.najdiStudenta(id);
        if (student != null) {
            student.provedSpecialniDovednost();
        } else {
            System.out.println("Student nenalezen");
        }
    }

    private static void vypisVsechnyStudenty() {
        System.out.println("\nVšichni studenti (abecedne podle prijmeni):");
        List<Student> vsechnyStudenty = studentDatabaze.getVsechnyStudenty();
        Collections.sort(vsechnyStudenty);
        
        if (vsechnyStudenty.isEmpty()) {
            System.out.println("V databazi nejsou zadni studenti");
        } else {
            vsechnyStudenty.forEach(System.out::println);
        }
    }

    private static void vypisStudentyTelekomunikace() {
        System.out.println("\nStudenti telekomunikaci:");
        List<Student> studenti = studentDatabaze.getStudentyTelekomunikace();
        Collections.sort(studenti);
        
        if (studenti.isEmpty()) {
            System.out.println("Zadni studenti telekomunikaci");
        } else {
            studenti.forEach(System.out::println);
        }
    }

    private static void vypisStudentyKyberBezpecnosti() {
        System.out.println("\nStudenti kyberneticke bezpecnosti:");
        List<Student> studenti = studentDatabaze.getStudentyKyberBezpecnosti();
        Collections.sort(studenti);
        
        if (studenti.isEmpty()) {
            System.out.println("Zadni studenti kyberneticke bezpecnosti");
        } else {
            studenti.forEach(System.out::println);
        }
    }

    private static void vypisPrumeryOboru() {
        System.out.println("\nPrumery podle oboru:");
        System.out.printf("Telekomunikace: %.2f%n", studentDatabaze.getPrumerTelekomunikace());
        System.out.printf("Kyberneticka bezpecnost: %.2f%n", studentDatabaze.getPrumerKyberBezpecnosti());
    }

    private static void vypisPoctyStudentu() {
        System.out.println("\nPocty studentu podle oboru:");
        System.out.println("Telekomunikace: " + studentDatabaze.getPocetStudentuTelekomunikace());
        System.out.println("Kyberneticka bezpecnost: " + studentDatabaze.getPocetStudentuKyberBezpecnosti());
    }

    private static void ulozStudentaDoSouboru() throws IOException {
        System.out.print("\nZadejte ID studenta k ulozeni: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 
        
        System.out.print("Zadejte nazev souboru: ");
        String nazevSouboru = scanner.nextLine();
        
        studentDatabaze.ulozStudentaDoSouboru(id, nazevSouboru);
        System.out.println("Student uspesne ulozen do souboru");
    }

    private static void nactiStudentaZeSouboru() throws IOException, ClassNotFoundException {
        System.out.print("\nZadejte nazev souboru k nacteni: ");
        String nazevSouboru = scanner.nextLine();
        
        Student student = studentDatabaze.nactiStudentaZeSouboru(nazevSouboru);
        studentDatabaze.pridejStudenta(student);
        System.out.println("Student uspesne nacten ze souboru. Nove ID: " + student.getId());
    }
}
