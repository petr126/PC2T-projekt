import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class StudentDatabazeImpl implements StudentDatabaze {
    private final List<Student> studentiTelekomunikace = new ArrayList<>();
    private final List<Student> studentiKyberBezpecnosti = new ArrayList<>();
    private int dalsiId = 1;
    private final String dbUrl;
    private final String dbUzivatel;
    private final String dbHeslo;

    public StudentDatabazeImpl(String dbUrl, String dbUzivatel, String dbHeslo) {
        this.dbUrl = dbUrl;
        this.dbUzivatel = dbUzivatel;
        this.dbHeslo = dbHeslo;
    }

    @Override
    public void pridejStudenta(Student student) {
        if (student instanceof StudentTelekomunikaci) {
            studentiTelekomunikace.add(student);
        } else if (student instanceof StudentKyberBezpecnosti) {
            studentiKyberBezpecnosti.add(student);
        }
        dalsiId = Math.max(dalsiId, student.getId() + 1);
    }

    @Override
    public void odeberStudenta(int id) {
        Student student = najdiStudenta(id);
        if (student != null) {
            if (student instanceof StudentTelekomunikaci) {
                studentiTelekomunikace.remove(student);
            } else if (student instanceof StudentKyberBezpecnosti) {
                studentiKyberBezpecnosti.remove(student);
            }
        }
    }

    @Override
    public Student najdiStudenta(int id) {
        for (Student student : studentiTelekomunikace) {
            if (student.getId() == id) {
                return student;
            }
        }
        for (Student student : studentiKyberBezpecnosti) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    @Override
    public List<Student> getVsechnyStudenty() {
        List<Student> vsechnyStudenty = new ArrayList<>();
        vsechnyStudenty.addAll(studentiTelekomunikace);
        vsechnyStudenty.addAll(studentiKyberBezpecnosti);
        return vsechnyStudenty;
    }

    @Override
    public List<Student> getStudentyTelekomunikace() {
        return new ArrayList<>(studentiTelekomunikace);
    }

    @Override
    public List<Student> getStudentyKyberBezpecnosti() {
        return new ArrayList<>(studentiKyberBezpecnosti);
    }

    @Override
    public void pridejZnamkuStudentovi(int id, int znamka) {
        Student student = najdiStudenta(id);
        if (student != null) {
            student.pridejZnamku(znamka);
        } else {
            throw new IllegalArgumentException("Student s ID " + id + " nebyl nalezen");
        }
    }

    @Override
    public double getPrumerTelekomunikace() {
        if (studentiTelekomunikace.isEmpty()) {
            return 0.0;
        }
        return studentiTelekomunikace.stream()
                .mapToDouble(Student::getPrumer)
                .average()
                .orElse(0.0);
    }

    @Override
    public double getPrumerKyberBezpecnosti() {
        if (studentiKyberBezpecnosti.isEmpty()) {
            return 0.0;
        }
        return studentiKyberBezpecnosti.stream()
                .mapToDouble(Student::getPrumer)
                .average()
                .orElse(0.0);
    }

    @Override
    public int getPocetStudentuTelekomunikace() {
        return studentiTelekomunikace.size();
    }

    @Override
    public int getPocetStudentuKyberBezpecnosti() {
        return studentiKyberBezpecnosti.size();
    }

    @Override
    public void ulozStudentaDoSouboru(int id, String nazevSouboru) throws IOException {
        Student student = najdiStudenta(id);
        if (student == null) {
            throw new IllegalArgumentException("Student s ID " + id + " nebyl nalezen");
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nazevSouboru))) {
            oos.writeObject(student);
        }
    }

    @Override
    public Student nactiStudentaZeSouboru(String nazevSouboru) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nazevSouboru))) {
            return (Student) ois.readObject();
        }
    }

    @Override
    public void ulozVseDoDatabaze() {
        try (Connection spojeni = DriverManager.getConnection(dbUrl, dbUzivatel, dbHeslo)) {
            // Smazat existujici data
            try (Statement stmt = spojeni.createStatement()) {
                stmt.executeUpdate("DELETE FROM studenti");
            }

            // Vlozit vsechny studenty
            String sql = "INSERT INTO studenti (id, jmeno, prijmeni, rokNarozeni, obor, znamky) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = spojeni.prepareStatement(sql)) {
                for (Student student : getVsechnyStudenty()) {
                    pstmt.setInt(1, student.getId());
                    pstmt.setString(2, student.getJmeno());
                    pstmt.setString(3, student.getPrijmeni());
                    pstmt.setInt(4, student.getRokNarozeni());
                    
                    String obor = (student instanceof StudentTelekomunikaci) ? "TELEKOM" : "KYBER";
                    pstmt.setString(5, obor);
                    
                    // Serializovat znamky jako retezec oddeleny carkami
                    String znamkyStr = student.getZnamky().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","));
                    pstmt.setString(6, znamkyStr);
                    
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Chyba pri ukladani do databaze: " + e.getMessage());
        }
    }

    @Override
    public void nactiVseZDatabaze() {
        studentiTelekomunikace.clear();
        studentiKyberBezpecnosti.clear();
        dalsiId = 1;

        try (Connection spojeni = DriverManager.getConnection(dbUrl, dbUzivatel, dbHeslo)) {
            String sql = "SELECT id, jmeno, prijmeni, rokNarozeni, obor, znamky FROM studenti";
            try (Statement stmt = spojeni.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String jmeno = rs.getString("jmeno");
                    String prijmeni = rs.getString("prijmeni");
                    int rokNarozeni = rs.getInt("rokNarozeni");
                    String obor = rs.getString("obor");
                    String znamkyStr = rs.getString("znamky");
                    
                    Student student;
                    if ("TELEKOM".equals(obor)) {
                        student = new StudentTelekomunikaci(id, jmeno, prijmeni, rokNarozeni);
                    } else {
                        student = new StudentKyberBezpecnosti(id, jmeno, prijmeni, rokNarozeni);
                    }
                    
                    // Parsovat znamky
                    if (znamkyStr != null && !znamkyStr.isEmpty()) {
                        String[] znamkyArray = znamkyStr.split(",");
                        for (String znamkaStr : znamkyArray) {
                            student.pridejZnamku(Integer.parseInt(znamkaStr.trim()));
                        }
                    }
                    
                    pridejStudenta(student);
                    dalsiId = Math.max(dalsiId, id + 1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Chyba pri nacitani z databaze: " + e.getMessage());
        }
    }

    public int getDalsiId() {
        return dalsiId;
    }
}