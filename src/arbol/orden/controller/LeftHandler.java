package arbol.orden.controller;

import arbol.orden.ArbolOrden;
import static arbol.orden.view.NodeCreator.createElipse;
import java.util.Map.Entry;
import javafx.scene.layout.StackPane;
import arbol.orden.model.Nodo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import static arbol.orden.model.TraverseTree.*;

public class LeftHandler implements EventHandler<MouseEvent> {

    private final Nodo<Entry<StackPane, String>> nodo;
    private final ArbolOrden app;
    private final Map<Object, Nodo<Entry<StackPane, String>>> map;
    private final List<Node> inorden;
    private final List<Node> postorden;
    private final List<Node> preorden;

    public LeftHandler(Nodo<Entry<StackPane, String>> nodo, ArbolOrden app, List<Node> inorden, List<Node> postorden, List<Node> preorden, Map<Object, Nodo<Entry<StackPane, String>>> map) {
        this.map = map;
        this.app = app;
        this.nodo = nodo;

        this.inorden = inorden;
        this.postorden = postorden;
        this.preorden = preorden;
    }

    @Override
    public void handle(MouseEvent t) {
        Entry<StackPane, String> entry = createElipse();
        
        Object source = t.getSource();
        Nodo<Entry<StackPane, String>> parent = map.get(source);
        
        parent.setLeft(new Nodo<>(entry));

        List<String> preorder = preorderTree(nodo, new ArrayList<String>());
        List<String> inorder = inorderTree(nodo, new ArrayList<String>());
        List<String> postorder = postorderTree(nodo, new ArrayList<String>());

        this.preorden.clear();
        this.preorden.addAll(stringListToNodeList(preorder));
        this.postorden.clear();
        this.postorden.addAll(stringListToNodeList(postorder));
        this.inorden.clear();
        this.inorden.addAll(stringListToNodeList(inorder));
        app.crearTablero();
    }
}
