import java.awt.Color;
import java.awt.geom.Ellipse2D;

/** Klasa do tworzenia koła */
public class OneEllipse extends Ellipse2D.Float {

    /** Zmienna z kolorem */
    public Color color;

    /** Zmienna, co wkazuje na szubkość modyfikacji figury */
    private double SCALE = 0.1;

    /** Kostuktor klasy */
    public OneEllipse(float x, float y, float width, float height) {
        setFrame(x, y, width, height);
        this.color = Color.BLACK;
    }

    /** Metoda, która sprawdza, czy kliknięcie było nad kołem */
    public boolean isHit(float x, float y) {
        return contains(x, y);
    }

    /** Metoda zmieniająca współrzędna x*/
    public void addX(float x) {
        this.x += x;
    }

    /** Metoda zmieniająca współrzędma y */
    public void addY(float y) {
        this.y += y;
    }

    /** Metoda zmieniająca szerokość */
    public void addWidth(float w) {
        this.width *= (1 + SCALE*w);
    }

    /** Metoda zmieniająca wysokość */
    public void addHeight(float h) {
        this.height *= (1 + SCALE*h);
    }
}