import java.io.Serializable;
/**
 * Klasa określająca uniwersalną figurę
 */
public class OneShape implements Serializable{

    /** Koło */
    public OneEllipse ellipse;

    /** Prostokąt */
    public OneRectangle rectangle;

    /** Trójkąt */
    public OneTriangle triangle;

    /** Rodzaj figury */
    public ShapeType type;

    /** Kostruktor Figury */
    public OneShape(OneShape.ShapeType typ){
        switch (typ) {
            case CIRCLE:
                this.rectangle = null;
                this.triangle = null;
                this.type = typ;
                break;
            case RECTANGLE:
                this.ellipse = null;
                this.triangle = null;
                this.type = typ;
                break;
            case TRIANGLE:
                this.ellipse = null;
                this.rectangle = null;
                this.type = typ;

            default:
                break;
        }
    }

    /** Klasa enum do określenia typu figury */
    public enum ShapeType{
        CIRCLE, RECTANGLE, TRIANGLE;
    }
}
