package pwr.swd.ahp;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button rankButton;

    @FXML
    private ListView<String> rankView;

    @FXML
    private TextField maxPrice;

    @FXML
    private TextField maxDeliveryTime;

    @FXML
    private TextField wantedToppings;

    @FXML
    private TextField unwantedToppings;

    @FXML
    private ChoiceBox<String> priceToTime;

    @FXML
    private ChoiceBox<String> priceToToppings;

    @FXML
    private ChoiceBox<String> timeToToppings;

    @FXML
    private ChoiceBox<String> priceToSize;

    @FXML
    private ChoiceBox<String> timeToSize;

    @FXML
    private ChoiceBox<String> sizeToToppings;

    @FXML
    void load(final ActionEvent event) {

    }

    @FXML
    void rank(final ActionEvent event) {

    }

    @FXML
    void initialize() {
        assertInitiated();
        initializeChoicesList();
        setChoices();

        rankButton.disableProperty().bind(Bindings.isEmpty(rankView.getItems()));
    }

    private void assertInitiated() {
        assert rankButton != null : "fx:id=\"rankButton\" was not injected: check your FXML file 'gui.fxml'.";
        assert rankView != null : "fx:id=\"rankView\" was not injected: check your FXML file 'gui.fxml'.";
        assert maxPrice != null : "fx:id=\"maxPrice\" was not injected: check your FXML file 'gui.fxml'.";
        assert maxDeliveryTime != null : "fx:id=\"maxDeliveryTime\" was not injected: check your FXML file 'gui.fxml'.";
        assert wantedToppings != null : "fx:id=\"wantedToppings\" was not injected: check your FXML file 'gui.fxml'.";
        assert unwantedToppings != null : "fx:id=\"unwantedToppings\" was not injected: check your FXML file 'gui.fxml'.";
        assert priceToTime != null : "fx:id=\"priceToTime\" was not injected: check your FXML file 'gui.fxml'.";
        assert priceToToppings != null : "fx:id=\"priceToToppings\" was not injected: check your FXML file 'gui.fxml'.";
        assert timeToToppings != null : "fx:id=\"timeToToppings\" was not injected: check your FXML file 'gui.fxml'.";
        assert priceToSize != null : "fx:id=\"priceToSize\" was not injected: check your FXML file 'gui.fxml'.";
        assert timeToSize != null : "fx:id=\"timeToSize\" was not injected: check your FXML file 'gui.fxml'.";
        assert sizeToToppings != null : "fx:id=\"sizeToToppings\" was not injected: check your FXML file 'gui.fxml'.";
    }

    private void initializeChoicesList() {
        final List<String> choices = Arrays.asList("1/9", "1/8", "1/7", "1/6", "1/5", "1/4", "1/3", "1/2",
                                                   "1", "2", "3", "4", "5", "6", "7", "8", "9");
        priceToTime.getItems().addAll(choices);
        priceToToppings.getItems().addAll(choices);
        timeToToppings.getItems().addAll(choices);
        priceToSize.getItems().addAll(choices);
        timeToSize.getItems().addAll(choices);
        sizeToToppings.getItems().addAll(choices);
    }

    private void setChoices() {
        priceToTime.setValue("1");
        priceToToppings.setValue("1");
        timeToToppings.setValue("1");
        priceToSize.setValue("1");
        timeToSize.setValue("1");
        sizeToToppings.setValue("1");
    }

}
