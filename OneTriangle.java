import java.awt.*;
import java.awt.geom.GeneralPath;
import java.io.Serializable;

/** Klasa do tworzenia trójkąta */
public class OneTriangle implements Serializable{

    /** Objekt tworżący trójkąt */
    public GeneralPath trojkat;

    /** Tablica przyhowująca współrzędne trójkąta*/
    public double points[][] = new double[3][2];

    /** Zmienna z kolorem */
    public Color color;

    /** Zmienna, co wkazuje na szubkość modyfikacji figury */
    private double SCALE = 0.1;

    /** Kostuktor klasy */
    public OneTriangle(double x1, double y1, double x2, double y2, double x3, double y3){
        this.points[0][0] = x1; this.points[0][1] = y1;
        points[1][0] = x2; points[1][1] = y2;
        points[2][0] = x3; points[2][1] = y3;
        this.color = Color.BLACK;
        make();

    }

    /** Metoda tworząca trójkąt z współrzędnuch wierzchołków trójkąt */
    public void make(){
        trojkat = new GeneralPath();
        trojkat.moveTo(points[0][0],points[0][1]);
        for (int i = 1; i < 3; i++)
            trojkat.lineTo(points[i][0], points[i][1]);

        trojkat.closePath();
    }

    /** Metoda, która sprawdza, czy kliknięcie było nad trójkątem */
    public boolean isHit(float x, float y) {
        return trojkat.contains(x, y);
    }

    /** Metoda zmieniająca współrzędna x*/
    public void addX(float x){
        for(int i = 0; i < 3; i++){
            points[i][0] += x;
        }
    }

    /** Metoda zmieniająca współrzędma y */
    public void addY(float y){
        for(int i = 0; i < 3; i++){
            points[i][1] += y;
        }
    }
    /** Metoda zmieniająca rozmiar */
    public void changeSize(float s) {

        double x = points[0][0];
        double y = points[0][1];

        for(int i = 0; i < 3; i++){
            points[i][0] *= (1 + SCALE*s);
            points[i][1] *= (1 + SCALE*s);
        }
        addX((float)(x - points[0][0]));
        addY((float)(y - points[0][1]));
    }
}