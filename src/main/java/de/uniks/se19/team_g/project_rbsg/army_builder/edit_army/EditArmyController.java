package de.uniks.se19.team_g.project_rbsg.army_builder.edit_army;

import de.uniks.se19.team_g.project_rbsg.configuration.ArmyIcon;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class EditArmyController implements Initializable {

    public ListView<ArmyIcon> iconList;
    public ImageView selectedIcon;
    public TextField nameInput;
    public Label symbolLabel;
    public Label nameLabel;
    public Label formTitle;
    public Button cancelButton;
    public Button submitButton;

    private Runnable onClose;

    private Army army;

    @Nonnull
    private final IconCellFactory iconCellFactory;
    @Nonnull
    private final Property<Locale> selectedLocale;

    public EditArmyController(
        @Nonnull IconCellFactory iconCellFactory,
        @Nonnull Property<Locale> selectedLocale
    ) {
        this.iconCellFactory = iconCellFactory;
        this.selectedLocale = selectedLocale;
    }

    public void setArmy(Army army) {
        this.army = army;

        resetFormFields();
    }

    private void resetFormFields() {
        nameInput.setText(army.name.get());
        iconList.getSelectionModel().select(army.iconType.get());
    }

    public void onConfirm() {

        boolean dirty = false;

        final String nextName = nameInput.getText();
        if (!nextName.equals(army.name.get())) {
            army.name.set(nextName);
            dirty = true;
        }

        final ArmyIcon nextIcon = iconList.getSelectionModel().getSelectedItem();
        if (nextIcon == army.iconType.get()) {
            army.iconType.set(nextIcon);
            dirty = true;
        }

        if (dirty) {
            army.setUnsavedUpdates(true);
        }

        close();
    }

    public void onCancel() {
        resetFormFields();
        close();
    }

    public void close() {
        if (onClose != null) {
            onClose.run();
        }
    }

    public void setOnClose(Runnable onClose) {

        this.onClose = onClose;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JavaFXUtils.setButtonIcons(
                submitButton,
                getClass().getResource("/assets/icons/operation/saveWhite.png"),
                getClass().getResource("/assets/icons/operation/saveBlack.png"),
                40
        );
        JavaFXUtils.setButtonIcons(
                cancelButton,
                getClass().getResource("/assets/icons/navigation/crossWhite.png"),
                getClass().getResource("/assets/icons/navigation/crossBlack.png"),
                40
        );

        iconList.setCellFactory(iconCellFactory);

        iconList.getItems().setAll( ArmyIcon.values());

        final ReadOnlyObjectProperty<ArmyIcon> selectionProperty = iconList.getSelectionModel().selectedItemProperty();

        selectedIcon.imageProperty().bind(Bindings.createObjectBinding(() -> {
            final ArmyIcon selected = selectionProperty.get();
            if (selected == null) return null;
            return selectionProperty.get().getImage();
        }, selectionProperty));

        formTitle.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "armyBuilder.editArmy.title"));
        nameLabel.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "armyBuilder.editArmy.nameField"));
        symbolLabel.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "armyBuilder.editArmy.symbolField"));
    }
}
