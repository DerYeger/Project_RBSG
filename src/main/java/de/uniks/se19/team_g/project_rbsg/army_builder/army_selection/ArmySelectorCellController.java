package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.model.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

import java.net.*;
import java.util.*;

@Component
@Scope("prototype")
public class ArmySelectorCellController extends ListCell<Army> implements Initializable {

    public Node root;
    public ImageView imageView;
    private SimpleObjectProperty<Army> hoveredArmy = null;

    @Override
    protected void updateItem(Army item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            return;
        }

        setGraphic(root);
        imageView.imageProperty().bind(
            Bindings.createObjectBinding(() -> item.iconType.get().getImage(), item.iconType)
        );

        if(hoveredArmy == null) {
            System.out.println("IM NULL");
        }

//        this.onMouseEnteredProperty().addListener(this::MouseHovered);
//        this.onMouseExitedProperty().addListener(this::MouseHoverLeft);

       setOnMouseEntered(this::MouseEntered);
        setOnMouseExited(this::MouseExited);
    }

    private void MouseExited (MouseEvent mouseEvent)
    {
        System.out.println("Hoverout!");
        hoveredArmy.set(null);
    }

    private void MouseEntered (MouseEvent mouseEvent)
    {
        System.out.println("Hoverin!");
        hoveredArmy.set(this.getItem());
    }

//    private void MouseHoverLeft (Observable observable)
//    {
//        System.out.println("Hoverout!");
//        hoveredArmy.set(null);
//    }
//
//    private void MouseHovered (Observable observable)
//    {
//        System.out.println("Hoverin!");
//        hoveredArmy.set(this.getItem());
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText(null);
        setGraphic(null);
    }

    public void setHoverProperty (SimpleObjectProperty<Army> hoverProperty)
    {
        this.hoveredArmy = hoverProperty;
    }
}
