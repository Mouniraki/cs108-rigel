package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.DoublePredicate;
import java.util.function.UnaryOperator;

import static javafx.beans.binding.Bindings.when;

/**
 * Main class of the Rigel project. It is responsible of displaying sky as a graphical user interface.
 *
 * @author Mounir Raki (310287)
 * @author Nicolas Szwajcok (315213)
 */
public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    /**
     * Redefinition of the start() method of Application where the scene graph is defined.
     *
     * @param stage
     *          the stage to where we want to draw the graphical elements
     * @throws Exception
     *          anytime there is an exception thrown in the application
     */
    @Override
    public void start(Stage stage) throws Exception {
        try(InputStream catalogueStream = getClass().getResourceAsStream("/hygdata_v3.csv");
            InputStream asterismStream = getClass().getResourceAsStream("/asterisms.txt")){

            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(catalogueStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            ZonedDateTime actualTime = ZonedDateTime.of(
                    LocalDate.now(),
                    LocalTime.now(),
                    ZoneId.systemDefault()
            );
            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(actualTime);

            ObserverLocationBean observerLocationBean = new ObserverLocationBean();
            GeographicCoordinates observerLocation = GeographicCoordinates.ofDeg(6.57, 46.52);
            observerLocationBean.setCoordinates(observerLocation);

            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            HorizontalCoordinates centerCoordinates = HorizontalCoordinates.ofDeg(180.000000000001, 15);
            viewingParametersBean.setCenter(centerCoordinates);
            viewingParametersBean.setFieldOfViewDeg(100);

            SkyCanvasManager canvasManager = new SkyCanvasManager(
                    catalogue,
                    dateTimeBean,
                    observerLocationBean,
                    viewingParametersBean);

            TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);

            HBox controlBar = new HBox(
                    observationPos(observerLocationBean), new Separator(Orientation.VERTICAL),
                    observationInstant(dateTimeBean, timeAnimator), new Separator(Orientation.VERTICAL),
                    timeAnimation(dateTimeBean, timeAnimator), new Separator(Orientation.VERTICAL),
                    searchCelestialObject(canvasManager)
            );
            controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
            controlBar.setAlignment(Pos.CENTER);

            Canvas sky = canvasManager.canvas();
            Pane canvasPane = new Pane(sky);
            BorderPane mainPane = new BorderPane(
                    canvasPane,
                    controlBar, null,
                    informationBar(canvasManager, viewingParametersBean),
                    null
            );
            sky.widthProperty().bind(canvasPane.widthProperty());
            sky.heightProperty().bind(canvasPane.heightProperty());

            stage.setTitle("Rigel");
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setScene(new Scene(mainPane));
            stage.show();
            sky.requestFocus();
        }
    }

    private HBox observationPos(ObserverLocationBean observerLocationBean){
        Label lonLabel = new Label("Longitude (°) :");
        TextField lonField = new TextField();
        TextFormatter<Number> lonFormatter = coordFormatter(GeographicCoordinates::isValidLonDeg);
        lonField.setTextFormatter(lonFormatter);
        lonFormatter.valueProperty().bindBidirectional(observerLocationBean.lonDegProperty());
        lonField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        Label latLabel = new Label("Latitude (°) :");
        TextField latField = new TextField();
        TextFormatter<Number> latFormatter = coordFormatter(GeographicCoordinates::isValidLatDeg);
        latField.setTextFormatter(latFormatter);
        latFormatter.valueProperty().bindBidirectional(observerLocationBean.latDegProperty());
        latField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        HBox observationPos = new HBox(lonLabel, lonField, latLabel, latField);
        observationPos.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return observationPos;
    }

    private HBox observationInstant(DateTimeBean dateTimeBean, TimeAnimator timeAnimator){
        Label dateLabel = new Label("Date :");
        DatePicker datePicker = new DatePicker();
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());
        datePicker.setStyle("-fx-pref-width: 120;");

        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter);

        Label timeLabel = new Label("Heure :");
        TextField timeField = new TextField();
        timeField.setTextFormatter(timeFormatter);
        timeFormatter.valueProperty().bindBidirectional(dateTimeBean.timeProperty());
        timeField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");

        ObservableList<ZoneId> zoneIds = FXCollections.observableArrayList();

        for(String zoneIdName : ZoneId.getAvailableZoneIds())
            zoneIds.add(ZoneId.of(zoneIdName));

        ComboBox<ZoneId> zoneBox = new ComboBox<>(zoneIds.sorted());
        zoneBox.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());
        zoneBox.setStyle("-fx-pref-width: 180;");

        datePicker.disableProperty().bind(timeAnimator.runningProperty());
        timeField.disableProperty().bind(timeAnimator.runningProperty());
        zoneBox.disableProperty().bind(timeAnimator.runningProperty());

        HBox observationInstant = new HBox(dateLabel, datePicker, timeLabel, timeField, zoneBox);
        observationInstant.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return observationInstant;
    }

    private HBox timeAnimation(DateTimeBean dateTimeBean, TimeAnimator timeAnimator) throws IOException{
        String playString = "\uf04b";
        String pauseString = "\uf04c";
        String resetString = "\uf0e2";

        ObservableList<NamedTimeAccelerator> accelerators = FXCollections.observableArrayList(NamedTimeAccelerator.values());
        ChoiceBox<NamedTimeAccelerator> timeChoice = new ChoiceBox<>(accelerators);
        timeChoice.setValue(NamedTimeAccelerator.TIMES_300);
        timeAnimator.acceleratorProperty().bind(Bindings.select(timeChoice.valueProperty(), "accelerator"));

        try(InputStream fontStream = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf")) {
            Font buttonFont = Font.loadFont(fontStream, 15);

            Button resetButton = new Button(resetString);
            Button playPauseButton = new Button(playString);
            resetButton.setFont(buttonFont);
            playPauseButton.setFont(buttonFont);


            resetButton.setOnAction(e -> {
                ZonedDateTime actualTime = ZonedDateTime.of(
                        LocalDate.now(),
                        LocalTime.now(),
                        ZoneId.systemDefault()
                );
                dateTimeBean.setZonedDateTime(actualTime);
            });

            playPauseButton.setOnAction(e -> {
                if(!timeAnimator.getRunning())
                    timeAnimator.start();
                else
                    timeAnimator.stop();
            });

            playPauseButton.textProperty().bind(
                    when(timeAnimator.runningProperty())
                    .then(pauseString)
                    .otherwise(playString)
            );

            timeChoice.disableProperty().bind(timeAnimator.runningProperty());
            resetButton.disableProperty().bind(timeAnimator.runningProperty());

            HBox timeAnimation = new HBox(timeChoice, resetButton, playPauseButton);
            timeAnimation.setStyle("-fx-spacing: inherit;");
            return timeAnimation;
        }
    }

    private HBox searchCelestialObject(SkyCanvasManager manager){
        Label objectNameLabel = new Label("Rechercher un objet céleste : ");
        TextField objectNameField = new TextField();
        manager.objectNameProperty().bind(objectNameField.textProperty());
        objectNameField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
        HBox searchCelestialObject = new HBox(objectNameLabel, objectNameField);
        searchCelestialObject.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return searchCelestialObject;
    }

    private BorderPane informationBar(SkyCanvasManager manager, ViewingParametersBean viewingParametersBean){
        Text fovText = new Text();
        Text objectClosestText = new Text();
        Text mousePositionText = new Text();

        StringExpression fovExpression = Bindings.format(Locale.ROOT,
                "Champ de vue : %.1f°",
                viewingParametersBean.fieldOfViewDegProperty());

        StringExpression mousePositionExpression = Bindings.format(Locale.ROOT,
                "Azimut : %.2f°, hauteur : %.2f°",
                manager.mouseAzDegProperty(), manager.mouseAltDegProperty());

        ObjectBinding<CelestialObject> objectClosestBinding = manager.objectUnderMouseProperty();

        fovText.textProperty().bind(fovExpression);
        objectClosestBinding.addListener(
                (p, o, n) -> objectClosestText.setText(n != null ? n.info() : "")
        );
        mousePositionText.textProperty().bind(mousePositionExpression);

        BorderPane informationBar = new BorderPane(objectClosestText, null, mousePositionText, null, fovText);
        informationBar.setStyle("-fx-padding: 4; -fx-background-color: white;");
        return informationBar;
    }

    private TextFormatter<Number> coordFormatter(DoublePredicate predicate){
        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> coordFilter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newCoordDeg = stringConverter.fromString(newText).doubleValue();

                return predicate.test(newCoordDeg) ? change : null;
            } catch (Exception e){
                return null;
            }
        });

        return new TextFormatter<>(stringConverter, 0, coordFilter);
    }
}
