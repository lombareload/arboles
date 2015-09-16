package arbol.orden;

import arbol.orden.controller.LeftHandler;
import arbol.orden.controller.RightHandler;
import arbol.orden.model.Nodo;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static arbol.orden.view.NodeCreator.createElipse;
import static arbol.orden.view.NodeCreator.fakeElipse;
import static arbol.orden.view.NodeCreator.RADIO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import java.util.Map.Entry;
import javafx.scene.layout.StackPane;

public class ArbolOrden extends Application {

    private final Nodo<Entry<StackPane, String>> arbol = new Nodo<>(createElipse());
    private final Map<Object, Nodo<Entry<StackPane, String>>> map = new HashMap<>();
    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;
    private static final int V_OFFSET = 10;
    private static final int BASE_X = (WIDTH - RADIO) / 2;
    private static final int BASE_Y = RADIO + RADIO + V_OFFSET;
    private static final int DELTA = 60;
    private Stage primaryStage;
    
    public ArbolOrden() {
        inLabel.setLayoutX(20);
        preLabel.setLayoutX(20);
        postLabel.setLayoutX(20);
        inLabel.setLayoutY(450);
        preLabel.setLayoutY(500);
        postLabel.setLayoutY(550);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Orden Arbol");
        crearTablero();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void crearTablero() {
        Group root = pintarInterfaz();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
    }

    private Group pintarInterfaz() {
        Group root = new Group();
        paintLabelsOrders(root);
        if (arbol != null && arbol.getValue() != null) {
            paintNodos(root, arbol, BASE_X);
        }
        return root;
    }

    private void paintNodos(
            final Group root,
            final Nodo<Entry<StackPane, String>> nodo,
            final int x) {

        int nodeY = calcularLevel(nodo);
        int nodeX = x;
        nodo.getValue().getKey().setLayoutX(nodeX);
        nodo.getValue().getKey().setLayoutY(nodeY);

        MoveTo moveTo = new MoveTo(nodeX + RADIO, nodeY + RADIO);
        Nodo<Entry<StackPane, String>> left = nodo.getLeft();
        Nodo<Entry<StackPane, String>> right = nodo.getRight();

        if (left != null) {
            root.getChildren().add(
                    paintLine(
                            moveTo,
                            RADIO + x - (1 + left.calcularRightLongitud()) * DELTA,
                            RADIO + calcularLevel(nodo) + BASE_Y));
            paintNodos(root, left, x - (1 + left.calcularRightLongitud()) * DELTA);
        } else {
            paintFakeNodo(root, nodo, x - DELTA / 2, new LeftHandler(arbol, this, inorden, postorden, preorden, map));
        }

        if (right != null) {
            root.getChildren().add(
                    paintLine(
                            moveTo,
                            RADIO + x + (1 + right.calcularLeftLongitud()) * DELTA,
                            RADIO + calcularLevel(nodo) + BASE_Y));
            paintNodos(root, right, x + (1 + right.calcularLeftLongitud()) * DELTA);
        } else {
            paintFakeNodo(root, nodo, x + DELTA / 2, new RightHandler(arbol, this, inorden, postorden, preorden, map));
        }
        root.getChildren().add(nodo.getValue().getKey());
    }

    private void paintFakeNodo(final Group root, final Nodo<Entry<StackPane, String>> parent, int x, EventHandler handler) {
        Node fakeNode = fakeElipse();
        map.put(fakeNode, parent);
        fakeNode.setLayoutY(calcularLevel(parent) + BASE_Y);
        fakeNode.setLayoutX(x);
        fakeNode.setOnMouseClicked(handler);
        root.getChildren().add(fakeNode);
    }

    private int calcularLevel(Nodo<Entry<StackPane, String>> nodo) {
        return BASE_Y * (nodo.calcularAltura() + 1);
    }

    private Path paintLine(MoveTo moveTo, double x, double y) {
        Path path = new Path();
        path.getElements().add(moveTo);
        path.getElements().add(new LineTo(x, y));
        path.setStrokeWidth(3);
        return path;
    }

    private void paintLabelsOrders(final Group root) {
        root.getChildren().add(inLabel);
        paintOrders(root, 450, inorden);
        root.getChildren().add(preLabel);
        paintOrders(root, 500, preorden);
        root.getChildren().add(postLabel);
        paintOrders(root, 550, postorden);

    }

    private final Label preLabel = new Label("Preorden:");
    private final Label postLabel = new Label("Postorden:");
    private final Label inLabel = new Label("Inorden:");
    private final List<Node> inorden = new ArrayList<>();
    private final List<Node> postorden = new ArrayList<>();
    private final List<Node> preorden = new ArrayList<>();

    private void paintOrders(Group root, int y, List<Node> order) {
        for (int i = 0; i < order.size(); i++) {
            Node current = order.get(i);
            current.setLayoutY(y);
            int x = 100 + i * DELTA;
            current.setLayoutX(x);
            if (i + 1 < order.size()) {
                MoveTo moveTo = new MoveTo(x + RADIO, y + RADIO);

                root.getChildren().add(paintLine(moveTo, x + DELTA, y + RADIO));
            }
            root.getChildren().add(current);
        }
    }
}
