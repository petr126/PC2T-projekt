import java.io.*;
import java.util.*;
import java.util.Date;


abstract class Student implements Comparable<Student>, Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private final String jmeno;
    private final String prijmeni;
    private final int rokNarozeni;
    private final List<Integer> znamky;
    private final Date datumZapsani;

    public Student(int id, String jmeno, String prijmeni, int rokNarozeni) {
        this.id = id;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.rokNarozeni = rokNarozeni;
        this.znamky = new ArrayList<>();
        this.datumZapsani = new Date();
    }

    public int getId() {
        return id;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public int getRokNarozeni() {
        return rokNarozeni;
    }

    public void pridejZnamku(int znamka) {
        if (znamka >= 1 && znamka <= 5) {
            znamky.add(znamka);
        } else {
            throw new IllegalArgumentException("Znamka musi byt mezi 1 a 5");
        }
    }

    public double getPrumer() {
        if (znamky.isEmpty()) {
            return 0.0;
        }
        return znamky.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public List<Integer> getZnamky() {
        return new ArrayList<>(znamky);
    }

    public Date getDatumZapsani() {
        return datumZapsani;
    }

    public abstract void provedSpecialniDovednost();

    @Override
    public int compareTo(Student other) {
        return this.prijmeni.compareToIgnoreCase(other.prijmeni);
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Jmeno: %s %s, Rok narozeni: %d, Prumer: %.2f", 
                id, jmeno, prijmeni, rokNarozeni, getPrumer());
    }
}