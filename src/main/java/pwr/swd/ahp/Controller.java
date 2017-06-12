package pwr.swd.ahp;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import static pwr.swd.ahp.PreferencePair.*;

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

    private final ObservableList<Pizza> pizzas = FXCollections.observableArrayList();

    @FXML
    void load(final ActionEvent event) throws Exception {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        final File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            final List<Pizza> data = new ObjectMapper().readValue(selectedFile, new TypeReference<List<Pizza>>() {});
            pizzas.setAll(data);
            rankView.getItems().setAll(pizzas.stream().map(Pizza::toString).collect(Collectors.toList()));
        }
    }

    @FXML
    void rank(final ActionEvent event) {
        final EnumMap<PreferencePair, Double> preferences = new EnumMap<>(PreferencePair.class);
        preferences.put(PRICE_TO_TIME, parse(priceToTime.getValue()));
        preferences.put(PRICE_TO_SIZE, parse(priceToSize.getValue()));
        preferences.put(PRICE_TO_TOPPINGS, parse(priceToToppings.getValue()));
        preferences.put(TIME_TO_SIZE, parse(timeToSize.getValue()));
        preferences.put(TIME_TO_TOPPINGS, parse(timeToToppings.getValue()));
        preferences.put(SIZE_TO_TOPPINGS, parse(sizeToToppings.getValue()));

        final List<String> wanted = wantedToppings.getText().isEmpty() ?
                Collections.emptyList() : Arrays.asList(wantedToppings.getText().split("\\s*,\\s*"));
        final List<String> unwanted = unwantedToppings.getText().isEmpty() ?
                Collections.emptyList() : Arrays.asList(unwantedToppings.getText().split("\\s*,\\s*"));

        final PizzasFilter filter = new PizzasFilter(Double.parseDouble(maxPrice.getText()),
                                                     Integer.parseInt(maxDeliveryTime.getText()), wanted, unwanted);

        final List<Pizza> ranked = new AhpAlgorithm(preferences).rank(pizzas, filter);
        rankView.getItems().setAll(ranked.stream().map(Pizza::toString).collect(Collectors.toList()));
    }

    private double parse(final String ratio) {
        if (ratio.contains("/")) {
            final String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(ratio);
        }
    }

    @FXML
    void initialize() {
        assertInitiated();
        initializeChoicesList();
        setChoices();

        rankButton.disableProperty().bind(Bindings.isEmpty(pizzas));
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
