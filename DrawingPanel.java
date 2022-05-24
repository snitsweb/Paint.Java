import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.File;


/**
 * Klassa jako panel do rysowania
 */
public class DrawingPanel extends JPanel implements ActionListener {

    /**
     * Zmienna do wybierania typu działania
     * 1 - koło
     * 2 - prostokąt
     * 3 - trójkąt
     * 4 - modyfikacja
     * 5 - rotate
     */

    public int mode;
    public Popup popup;

    private ArrayList<OneShape> shapes = new ArrayList<OneShape>();
    private int shapeNum = 0;

    /**
     * Obsługa kliknięć
     */
    MoveCreateAdapter moveCreateAdapter;

    /**
     * Obsługa scrolla
     */
    ScalingAdapter scalingAdapter;
    ActionListener popupListener;

    /**
     * Konstruktor DrawingPanel
     */
    public DrawingPanel() {
        initUI();
    }

    /**
     * Funckja dla tworżenia Panelu
     */
    private void initUI() {
        popup = new Popup();
        popupListener = new ActionListener() {
            Color color = Color.BLACK;

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Blue")) {
                    color = Color.BLUE;
                } else if (e.getActionCommand().equals("Red")) {
                    color = Color.RED;
                } else if (e.getActionCommand().equals("Yellow")) {
                    color = Color.YELLOW;
                } else if (e.getActionCommand().equals("Pink")) {
                    color = Color.PINK;
                } else if (e.getActionCommand().equals("Green")) {
                    color = Color.GREEN;
                } else if (e.getActionCommand().equals("Orange")) {
                    color = Color.ORANGE;
                }

                switch (shapes.get(popup.selected).type) {
                    case RECTANGLE:
                        shapes.get(popup.selected).rectangle.color = color;
                        repaint();
                        break;

                    case CIRCLE:
                        shapes.get(popup.selected).ellipse.color = color;
                        repaint();
                        break;

                    case TRIANGLE:
                        shapes.get(popup.selected).triangle.color = color;
                        repaint();
                        break;
                }

                popup.selected = -1;
            }
        };


        String[] colors = new String[]{"Red", "Yellow", "Pink", "Green", "Orange"};
        JMenuItem item;
        for (int i = 0; i < colors.length; i++) {
            item = new JMenuItem(colors[i]);
            item.setHorizontalTextPosition(JMenuItem.LEFT);
            item.addActionListener(popupListener);
            popup.add(item);
        }

        moveCreateAdapter = new MoveCreateAdapter(this);
        scalingAdapter = new ScalingAdapter();
        addMouseMotionListener(moveCreateAdapter);
        addMouseListener(moveCreateAdapter);
        addMouseWheelListener(scalingAdapter);
    }

    /**
     * Funkcja do rysowania figur
     */
    private void doDrawing(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        System.out.println("DoDrawing");
        for (int i = 0; i < shapeNum; i++) {
            AffineTransform old = graphics2D.getTransform();
            switch (shapes.get(i).type) {
                case RECTANGLE:
                    if (shapes.get(i).rectangle.rotateDegres != 0) {
                        graphics2D.translate(shapes.get(i).rectangle.x, shapes.get(i).rectangle.y);
                        graphics2D.rotate(Math.toRadians(shapes.get(i).rectangle.rotateDegres));
                        graphics2D.translate(-(shapes.get(i).rectangle.x) - shapes.get(i).rectangle.width, -(shapes.get(i).rectangle.y) - shapes.get(i).rectangle.height);
                    }
                    graphics2D.setPaint(shapes.get(i).rectangle.color);
                    graphics2D.fill(shapes.get(i).rectangle);
                    break;

                case CIRCLE:
                    graphics2D.setPaint(shapes.get(i).ellipse.color);
                    graphics2D.fill(shapes.get(i).ellipse);
                    break;

                case TRIANGLE:
                    if(shapes.get(i).triangle.rotateDegres != 0){
                        graphics2D.translate(shapes.get(i).triangle.points[0][0], shapes.get(i).triangle.points[0][1]);
                        graphics2D.rotate(Math.toRadians(shapes.get(i).triangle.rotateDegres));
                    }
                    shapes.get(i).triangle.make();
                    graphics2D.setPaint(shapes.get(i).triangle.color);
                    graphics2D.draw(shapes.get(i).triangle.trojkat);
                    graphics2D.fill(shapes.get(i).triangle.trojkat);
                    break;

                default:
                    break;
            }

            graphics2D.setTransform(old);
        }
    }

    /**
     * Dodanie to standartowej metody i dodanie DoDrawing
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    public void rotateShape(OneShape shape, int i) {

        if (shape.type == OneShape.ShapeType.RECTANGLE) {
            shape.rectangle.rotateDegres += 15.0;
            repaint();
            System.out.println("EDITED");
        } else if (shape.type == OneShape.ShapeType.TRIANGLE) {
            shape.triangle.rotateDegres += 15.0;
            repaint();
        }
    }

    /**
     * Klasa do modyfikacji i tworzenia figur
     */
    class MoveCreateAdapter extends MouseAdapter {
        private DrawingPanel dp;
        private int x;
        private int y;

        /**
         * Zmienna co pokazuje stan tworzenia kola
         */
        private int ellNum;
        /**
         * Zmienna co pokazuje stan tworzenia prostokątą
         */
        private int rectNum;
        /**
         * Zmienna co pokazuje stan tworzenia trójkąta
         */
        private int trState;
        /**
         * Zmienna co chroni index ostatniego twożonego trójkąta
         */
        private int trLast;
        /**
         * Zmienna co pokazuje index aktywnej figury
         */
        private int current;

        MoveCreateAdapter(DrawingPanel dp) {
            this.dp = dp;
            ellNum = 0;
            rectNum = 0;
            trState = 0;
            current = -1;
        }

        /**
         * Obsługa kliknięcia myszy
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if (dp.mode == 1 && e.getButton() == 1) {
                x = e.getX();
                y = e.getY();
                ellNum = 1;
                shapes.add(new OneShape(OneShape.ShapeType.CIRCLE));
                shapeNum++;
                shapes.get(shapeNum - 1).ellipse = new OneEllipse(x, y, 0, 0);
                repaint();
            } else if (dp.mode == 2 && e.getButton() == 1) {
                x = e.getX();
                y = e.getY();
                rectNum = 1;
                shapes.add(new OneShape(OneShape.ShapeType.RECTANGLE));
                shapeNum++;
                shapes.get(shapeNum - 1).rectangle = new OneRectangle(x, y, 0, 0);
                repaint();
            } else if (dp.mode == 3 && e.getButton() == 1) {
                if (trState == 0) {
                    x = e.getX();
                    y = e.getY();
                    shapes.add(new OneShape(OneShape.ShapeType.TRIANGLE));
                    trState = 1;
                    shapeNum++;
                    trLast = shapeNum;
                    shapes.get(shapeNum - 1).triangle = new OneTriangle(x, y, x, y, x, y);
                    System.out.println("zaczynam tworzyc trójkąt");
                    repaint();
                }
                if (trState == 2) {
                    trState = 3;
                    x = e.getX();
                    y = e.getY();
                    shapes.get(trLast - 1).triangle.points[2][0] = x;
                    shapes.get(trLast - 1).triangle.points[2][1] = y;
                    System.out.println("COntynuje");
                    repaint();
                }
            } else if (dp.mode == 4 && e.getButton() == 1) {
                x = e.getX();
                y = e.getY();
                boolean isHit = false;
                for (int i = 0; !isHit && i < shapeNum; i++) {
                    switch (shapes.get(i).type) {
                        case RECTANGLE:
                            if (shapes.get(i).rectangle.isHit(x, y)) {
                                current = i;
                                isHit = true;
                            }
                            break;

                        case CIRCLE:
                            if (shapes.get(i).ellipse.isHit(x, y)) {
                                current = i;
                                isHit = true;
                            }
                            break;

                        case TRIANGLE:
                            if (shapes.get(i).triangle.isHit(x, y)) {
                                current = i;
                                isHit = true;
                            }
                            break;

                        default:
                            break;
                    }
                }
                if (!isHit) current = -1;
            }

            togglePopup(e);

        }

        /**
         * Obsługa przesunięcia myszy
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            if (dp.mode == 1 && ellNum == 1) {
                int dx = x - e.getX();
                int dy = y - e.getY();
                int result = Math.min(Math.abs(dx), Math.abs(dy));
                if (dx != 0 && dy != 0) {
                    shapes.get(shapeNum - 1).ellipse.setFrameFromDiagonal(x, y, x - result * Math.abs(dx) / (dx), y - result * Math.abs(dy) / (dy));
                    repaint();
                }
            } else if (dp.mode == 2 && rectNum == 1) {
                shapes.get(shapeNum - 1).rectangle.setFrameFromDiagonal(x, y, e.getX(), e.getY());
                repaint();
            } else if (dp.mode == 3 && trState == 1) {
                shapes.get(shapeNum - 1).triangle.points[1][0] = e.getX();
                shapes.get(shapeNum - 1).triangle.points[1][1] = e.getY();
                repaint();
            } else if (dp.mode == 3 && trState == 3) {
                shapes.get(trLast - 1).triangle.points[2][0] = e.getX();
                shapes.get(trLast - 1).triangle.points[2][1] = e.getY();
                repaint();
            } else if (dp.mode == 4 && current != 1) {
                int dx = e.getX() - x;
                int dy = e.getY() - y;

                switch (shapes.get(current).type) {
                    case CIRCLE:
                        shapes.get(current).ellipse.addX(dx);
                        shapes.get(current).ellipse.addY(dy);
                        repaint();
                        break;

                    case RECTANGLE:
                        shapes.get(current).rectangle.addX(dx);
                        shapes.get(current).rectangle.addY(dy);
                        repaint();
                        break;

                    case TRIANGLE:
                        shapes.get(current).triangle.addX(dx);
                        shapes.get(current).triangle.addY(dy);
                        repaint();
                        break;

                    default:
                        break;
                }

                x = e.getX();
                y = e.getY();
            }
        }

        /**
         * Obsługa odciśniecią myszy
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            if (dp.mode == 1 && ellNum == 1) {
                ellNum = 0;
            } else if (dp.mode == 2 && rectNum == 1) {
                rectNum = 0;
            } else if (dp.mode == 3 && trState == 1) {
                trState = 2;
            } else if (dp.mode == 3 && trState == 3) {
                trState = 0;
            } else if (dp.mode == 4) {
                current = -1;
                togglePopup(e);
            }
            togglePopup(e);
        }

        /**
         * Obsługa kliku muszy
         */
        public void mouseClicked(MouseEvent e) {
            togglePopup(e);
        }

        /**
         * Obsługa popupu z colorami
         */
        private void togglePopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                x = e.getX();
                y = e.getY();

                boolean isHit = false;

                popup.selected = -1;
                for (int i = 0; !isHit && i < shapeNum; i++) {
                    switch (shapes.get(i).type) {
                        case CIRCLE:
                            if (shapes.get(i).ellipse.isHit(x, y)) {
                                popup.selected = i;
                                isHit = true;
                            }
                            break;

                        case RECTANGLE:
                            if (shapes.get(i).rectangle.isHit(x, y)) {
                                popup.selected = i;
                                isHit = true;
                            }
                            break;

                        case TRIANGLE:
                            if (shapes.get(i).triangle.isHit(x, y)) {
                                popup.selected = i;
                                isHit = true;
                            }
                            break;

                        default:
                            break;
                    }
                }

                if (popup.selected != -1) {
                    popup.show(DrawingPanel.this, e.getX(), e.getY());
                    System.out.println("POpup");
                }
            }
        }

    }

    /**
     * Klasa do obsługi scalowania myszką
     */
    class ScalingAdapter implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                boolean isHit = false;
                if (mode == 4) {
                    for (int i = 0; !isHit && i <= shapeNum; i++) {
                        switch (shapes.get(i).type) {
                            case CIRCLE:
                                if (shapes.get(i).ellipse.isHit(x, y)) {
                                    float amount = e.getWheelRotation();
                                    shapes.get(i).ellipse.addHeight(amount);
                                    shapes.get(i).ellipse.addWidth(amount);
                                    repaint();
                                    isHit = true;
                                }
                                break;

                            case RECTANGLE:
                                if (shapes.get(i).rectangle.isHit(x, y)) {
                                    float amount = e.getWheelRotation();
                                    shapes.get(i).rectangle.addHeight(amount);
                                    shapes.get(i).rectangle.addWidth(amount);
                                    repaint();

                                    isHit = true;
                                }
                                break;

                            case TRIANGLE:
                                if (shapes.get(i).triangle.isHit(x, y)) {
                                    float amount = e.getWheelRotation();
                                    shapes.get(i).triangle.changeSize(amount);
                                    repaint();
                                    isHit = true;
                                }
                                break;

                            default:
                                break;
                        }
                    }
                } else if (mode == 5) {

                    for (int i = 0; !isHit && i < shapeNum; i++) {
                        switch (shapes.get(i).type) {
                            case CIRCLE:
                                isHit = true;
                                break;
                            case TRIANGLE:
                                if (shapes.get(i).triangle.isHit(x, y)) {
                                    System.out.println("Current: " + shapes.get(i));
                                    rotateShape(shapes.get(i), i);
                                    repaint();
                                    isHit = true;
                                }
                                break;
                            case RECTANGLE:
                                if (shapes.get(i).rectangle.isHit(x, y)) {
                                    System.out.println("Current: " + shapes.get(i));
                                    rotateShape(shapes.get(i), i);
                                    System.out.println("After changes: " + shapes.get(i));
                                    repaint();
                                    isHit = true;
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Obsługa przycisków menu
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Circle")) {
            mode = 1;
        } else if (e.getActionCommand().equals("Rectangle")) {
            mode = 2;
        } else if (e.getActionCommand().equals("Triangle")) {
            mode = 3;
        } else if (e.getActionCommand().equals("Edit")) {
            mode = 4;
        } else if (e.getActionCommand().equals("Rotate")) {
            mode = 5;
        }
    }

    class MousePopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            togglePopup(e);
        }

        public void mouseClicked(MouseEvent e) {
            togglePopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            togglePopup(e);
        }

        private void togglePopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(DrawingPanel.this, e.getX(), e.getY());
                System.out.println("POpup");
            }
        }
    }
}