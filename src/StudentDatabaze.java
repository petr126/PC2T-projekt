import java.io.IOException;
import java.util.List;

interface StudentDatabaze {
    void pridejStudenta(Student student);
    void odeberStudenta(int id);
    Student najdiStudenta(int id);
    List<Student> getVsechnyStudenty();
    List<Student> getStudentyTelekomunikace();
    List<Student> getStudentyKyberBezpecnosti();
    void pridejZnamkuStudentovi(int id, int znamka);
    double getPrumerTelekomunikace();
    double getPrumerKyberBezpecnosti();
    int getPocetStudentuTelekomunikace();
    int getPocetStudentuKyberBezpecnosti();
    void ulozStudentaDoSouboru(int id, String nazevSouboru) throws IOException;
    Student nactiStudentaZeSouboru(String nazevSouboru) throws IOException, ClassNotFoundException;
    void ulozVseDoDatabaze();
    void nactiVseZDatabaze();
}