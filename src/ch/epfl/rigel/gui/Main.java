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
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

/**
 * CLASSDESCRIPTION
 *
 * @author Mounir Raki (310287)
 */
public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try(InputStream catalogueStream = getClass().getResourceAsStream("/hygdata_v3.csv");
            InputStream asterismStream = getClass().getResourceAsStream("/asterisms.txt")){

            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(catalogueStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            DateTimeBean dateTimeBean = new DateTimeBean();
            ZonedDateTime actualTime = ZonedDateTime.of(
                    LocalDate.now(),
                    LocalTime.now(),
                    ZoneId.systemDefault()
            );
            dateTimeBean.setZonedDateTime(actualTime);

            ObserverLocationBean observerLocationBean = new ObserverLocationBean();
            GeographicCoordinates observerLocation = GeographicCoordinates.ofDeg(6.57, 46.52);
            observerLocationBean.setCoordinates(observerLocation);

            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            HorizontalCoordinates centerCoordinates = HorizontalCoordinates.ofDeg(180.000000000001, 15);
            viewingParametersBean.setCenter(centerCoordinates);
            viewingParametersBean.setFieldOfViewDeg(128.2);

            SkyCanvasManager canvasManager = new SkyCanvasManager(
                    catalogue,
                    dateTimeBean,
                    observerLocationBean,
                    viewingParametersBean);


            TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);

            Separator separator = new Separator();

            HBox controlBar = new HBox(
                    observationPos(), separator,
                    observationInstant(dateTimeBean, timeAnimator), separator, //ISSUE HERE
                    timeAnimation(timeAnimator) //ISSUE HERE
            );
            controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");


            Canvas sky = canvasManager.canvas();
            Pane canvasPane = new Pane(sky);

            BorderPane mainPane = new BorderPane(
                    canvasPane,
                    controlBar, null,
                    informationBar(canvasManager, viewingParametersBean),
                    null
            );

            sky.widthProperty().bind(mainPane.widthProperty());
            sky.heightProperty().bind(mainPane.heightProperty());

            stage.setTitle("Rigel");
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setScene(new Scene(mainPane));
            stage.show();
            sky.requestFocus();
        }
    }

    private HBox observationPos(){
        Label lonLabel = new Label("Longitude (°) :");
        TextField lonField = new TextField();
        lonField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        //TODO Set the textfield to accept only valid values

        Label latLabel = new Label("Latitude (°) :");
        TextField latField = new TextField();
        latField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        //TODO Set the textfield to accept only valid values

        HBox observationPos = new HBox(lonLabel, lonField, latLabel, latField);
        observationPos.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return observationPos;
    }

    private HBox observationInstant(DateTimeBean dateTimeBean, TimeAnimator timeAnimator){
        Label dateLabel = new Label("Date :");
        DatePicker datePicker = new DatePicker();
        datePicker.valueProperty().bind(dateTimeBean.dateProperty());
        datePicker.setStyle("-fx-pref-width: 120;");

        Label timeLabel = new Label("Heure :");
        TextField timeField = new TextField();
        //TODO Add a binding between the value property of timeFormatter and timeProperty()
        timeField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");


        ObservableList<ZoneId> zoneIds = FXCollections.observableArrayList();
        for(String zoneIdName : ZoneId.getAvailableZoneIds()) {
            zoneIds.add(ZoneId.of(zoneIdName));
        }

        ComboBox zoneBox = new ComboBox(zoneIds.sorted());
        zoneBox.valueProperty().bind(dateTimeBean.zoneProperty());
        zoneBox.setStyle("-fx-pref-width: 180;");

        datePicker.disableProperty().bind(timeAnimator.runningProperty());
        timeField.disableProperty().bind(timeAnimator.runningProperty());
        zoneBox.disableProperty().bind(timeAnimator.runningProperty());

        HBox observationInstant = new HBox(dateLabel, datePicker, timeLabel, timeField, zoneBox);
        observationInstant.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return observationInstant;
    }

    private HBox timeAnimation(TimeAnimator timeAnimator) throws IOException{
        ObservableList<NamedTimeAccelerator> accelerators = FXCollections.observableArrayList(NamedTimeAccelerator.values());
        ChoiceBox timeChoice = new ChoiceBox();
        timeChoice.setItems(accelerators);
        //TODO : Bind name with accelerator

        String resetString = "\uf0e2";
        String playString = "\uf04b";
        String pauseString = "\uf04c";

        try(InputStream fontStream = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf")) {
            Font buttonFont = Font.loadFont(fontStream, 15);

            Button resetButton = new Button(resetString);
            Button playPauseButton = new Button();

            if(timeAnimator.getRunning())
                playPauseButton.setText(pauseString);
            else
                playPauseButton.setText(playString);

            resetButton.setFont(buttonFont);
            playPauseButton.setFont(buttonFont);

            HBox timeAnimation = new HBox();
            timeAnimation.setStyle("-fx-spacing: inherit;");
            return timeAnimation;
        }
    }
    private BorderPane informationBar(SkyCanvasManager manager, ViewingParametersBean viewingParametersBean){
        ObjectBinding<CelestialObject> objectClosestBinding = manager.objectUnderMouseProperty();

        StringExpression fovExpression = Bindings.format(Locale.ROOT, "Champ de vue : %.1f°",
                viewingParametersBean.fieldOfViewDegProperty());

        StringExpression mousePositionExpression = Bindings.format(Locale.ROOT,
                "Azimut : %.2f°, hauteur : %.2f°",
                manager.mouseAzDegProperty(), manager.mouseAltDegProperty());

        Text fovText = new Text();
        Text objectClosestText = new Text();
        Text mousePositionText = new Text();

        fovExpression.addListener((p, o, n) -> fovText.setText(n)); //DOESNT CHANGE THE VALUE

        objectClosestBinding.addListener(
                (p, o, n) -> objectClosestText.setText(
                objectClosestBinding.get() != null ? objectClosestBinding.get().info() : ""
                )
        );

        mousePositionExpression.addListener((p, o, n) -> mousePositionText.setText(n)); //DOESNT CHANGE THE VALUE

        BorderPane informationBar = new BorderPane(objectClosestText, null, mousePositionText, null, fovText);
        informationBar.setStyle("-fx-padding: 4; -fx-background-color: white;");
        return informationBar;
    }

    /* FOR HOURS
    DateTimeFormatter hmsFormatter =
  DateTimeFormatter.ofPattern("HH:mm:ss");
LocalTimeStringConverter stringConverter =
  new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
TextFormatter<LocalTime> timeFormatter =
  new TextFormatter<>(stringConverter);
     */

    /* FOR LON AND LAT
        NumberStringConverter stringConverter =
                new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLonDeg =
                        stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLonDeg(newLonDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> lonTextFormatter =
                new TextFormatter<>(stringConverter, 0, lonFilter);

        TextField lonTextField =
                new TextField();
        lonTextField.setTextFormatter(lonTextFormatter);
     */
}
