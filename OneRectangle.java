import java.awt.*;
import java.awt.geom.*;


/** Klasa do tworzenia prostokąta */
 public class OneRectangle extends Rectangle2D.Float {

    /** Zmienna z kolorem */
    public Color color;
    public int rotateDegres = 0;

    /** Zmienna, co wkazuje na szubkość modyfikacji figury */
    private double SCALE = 0.1;

    /** Kostuktor klasy */
    public OneRectangle(float x, float y, float width, float height) {
        setRect(x, y, width, height);
        this.color = Color.BLACK;
    }

    /** Metoda, która sprawdza, czy kliknięcie było nad prostokątem */
    public boolean isHit(float x, float y) {
        return getBounds2D().contains(x, y);
    }

    /** Metoda, co ustawia szerokość */
    public void setWidth(float w){
        this.width = w;
    }

    /** Metoda, co ustawia wysokość */
    public void setHeight(float h){
        this.width = h;
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