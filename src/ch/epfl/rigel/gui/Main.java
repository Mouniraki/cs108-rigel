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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
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
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.DoublePredicate;
import java.util.function.UnaryOperator;

/**
 * Main class of the Rigel project. It is responsible of displaying sky graphical user interface.
 *
 * @author Nicolas Szwajcok (315213)
 */
public class Main extends Application {
    private final static ZonedDateTime CURRENT_TIME = ZonedDateTime.of(
            LocalDate.now(),
            LocalTime.now(),
            ZoneId.systemDefault()
    );

    public static void main(String[] args){
        launch(args);
    }

    private InputStream resourceStream() {
        return getClass().getResourceAsStream("/hygdata_v3.csv");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try (InputStream hs = resourceStream()) {
            InputStream asterismStream = getClass().getResourceAsStream("/asterisms.txt");
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            ObserverLocationBean observerLocationBean = new ObserverLocationBean();
            GeographicCoordinates observerLocation = GeographicCoordinates.ofDeg(6.57, 46.52);
            observerLocationBean.setCoordinates(observerLocation);

            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            HorizontalCoordinates centerCoord = HorizontalCoordinates.ofDeg(180.000000000001, 15);
            viewingParametersBean.setCenter(centerCoord);
            viewingParametersBean.setFieldOfViewDeg(100.0);

            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(CURRENT_TIME);

            BorderPane borderPane = new BorderPane();

            SkyCanvasManager skyCanvasManager = new SkyCanvasManager(
                    catalogue,
                    dateTimeBean,
                    observerLocationBean,
                    viewingParametersBean);

            skyCanvasManager.objectUnderMouseProperty().addListener(
                    (p, o, n) -> {
                        if (n != null) System.out.println(n);
                    });
            TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);

            Canvas skyCanvas = skyCanvasManager.canvas();
            skyCanvas.widthProperty().bind(borderPane.widthProperty());
            skyCanvas.heightProperty().bind(borderPane.heightProperty());

            HBox topBar = topBar(observerLocationBean, dateTimeBean, timeAnimator);

            Scene scene = new Scene(borderPane);
            Pane pane = new Pane(skyCanvas);

            borderPane.setTop(topBar);
            borderPane.setCenter(pane);
            borderPane.setBottom(bottomPane(skyCanvasManager, viewingParametersBean));

            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.setY(100);
            primaryStage.setScene(scene);
            primaryStage.show();
            skyCanvas.requestFocus();
        }
    }

    private HBox topBar(ObserverLocationBean observerLocationBean, DateTimeBean dateTimeBean, TimeAnimator timeAnimator) throws IOException {
        HBox toolbar = new HBox();
        toolbar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);

        toolbar.getChildren().add(positionBox(observerLocationBean));
        toolbar.getChildren().add(separator);
        toolbar.getChildren().add(observationInstant(dateTimeBean, timeAnimator));
        toolbar.getChildren().add(separator2);
        toolbar.getChildren().add(playBox(dateTimeBean, timeAnimator));

        return toolbar;
    }

    private HBox positionBox(ObserverLocationBean observerLocationBean){
        HBox positionBox = new HBox();
        positionBox.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        List<Node> lonNodes = positionFormatter("Longitude (°) :", observerLocationBean.lonDegProperty(), GeographicCoordinates::isValidLonDeg);
        List<Node> latNodes = positionFormatter("Latitude (°) :", observerLocationBean.latDegProperty(), GeographicCoordinates::isValidLatDeg);

        positionBox.getChildren().addAll(lonNodes);
        positionBox.getChildren().addAll(latNodes);
        return positionBox;
    }

    private  HBox observationInstant(DateTimeBean dateTimeBean, TimeAnimator timeAnimator){
        HBox observationInstantBox = new HBox();
        observationInstantBox.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label dateLabel = new Label("Date :");

        DatePicker datePicker = new DatePicker(dateTimeBean.getDate());
        dateTimeBean.dateProperty().bindBidirectional(datePicker.valueProperty());
        datePicker.disableProperty().bind(timeAnimator.runningProperty());
        datePicker.setStyle("-fx-pref-width: 120;");

        List<Node> timeNodes = timeFormatter(dateTimeBean.timeProperty(), timeAnimator);
        Set<String> zoneIds = ZoneId.getAvailableZoneIds();

        ObservableList<ZoneId> obsList = FXCollections.observableArrayList();
        for(String zoneId : zoneIds){
            obsList.add(ZoneId.of(zoneId));
        }
        ComboBox<ZoneId> timeZones = new ComboBox<>(obsList.sorted());
        timeZones.setValue(ZoneId.of(dateTimeBean.getZone().getId()));
        timeZones.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());
        timeZones.disableProperty().bind(timeAnimator.runningProperty());
        timeZones.setStyle("-fx-pref-width: 180;");

        observationInstantBox.getChildren().add(dateLabel);
        observationInstantBox.getChildren().add(datePicker);
        observationInstantBox.getChildren().addAll(timeNodes);
        observationInstantBox.getChildren().add(timeZones);

        return observationInstantBox;
    }

    private HBox playBox(DateTimeBean dateTimeBean, TimeAnimator timeAnimator) throws IOException {
        HBox playBox = new HBox();
        playBox.setStyle("-fx-spacing: inherit;"); //.setitems to arraylist -> observable, default value with choicebox.setvalue

        ObservableList<NamedTimeAccelerator> timeAccelerators = FXCollections.observableArrayList(NamedTimeAccelerator.TIMES_1,
                NamedTimeAccelerator.TIMES_30, NamedTimeAccelerator.TIMES_300, NamedTimeAccelerator.TIMES_3000, NamedTimeAccelerator.SIDEREAL_DAY, NamedTimeAccelerator.DAY);

        ChoiceBox<NamedTimeAccelerator> timeAcceleratorsBox = new ChoiceBox<>(timeAccelerators);
        timeAcceleratorsBox.setValue(NamedTimeAccelerator.TIMES_1);

        ObjectProperty<TimeAccelerator> currentAccelerator = timeAnimator.acceleratorProperty();
        currentAccelerator.bind(Bindings.select(timeAcceleratorsBox.valueProperty(), "accelerator"));
        timeAcceleratorsBox.disableProperty().bind(timeAnimator.runningProperty());

        try(InputStream fontStream = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf")) {
            Font fontAwesome = Font.loadFont(fontStream, 15);

            Button resetButton = new Button("\uf0e2");
            Button startButton = new Button("\uf04b");
            Button stopButton = new Button("\uf04c");
            resetButton.setFont(fontAwesome);
            resetButton.defaultButtonProperty();
            startButton.setFont(fontAwesome);
            stopButton.setFont(fontAwesome);

            resetButton.setOnAction(event -> {
                dateTimeBean.setZonedDateTime(ZonedDateTime.now());
                resetButton.disableProperty().bind(timeAnimator.runningProperty());
            });

            startButton.setOnAction(event -> {
                timeAnimator.start();
                playBox.getChildren().remove(startButton);
                playBox.getChildren().add(stopButton);

            });
            stopButton.setOnAction(event -> {
                timeAnimator.stop();
                playBox.getChildren().remove(stopButton);
                playBox.getChildren().add(startButton);
            });

            playBox.getChildren().add(timeAcceleratorsBox);
            playBox.getChildren().add(resetButton);
            playBox.getChildren().add(startButton);
        }

        return playBox;
    }

    private BorderPane bottomPane(SkyCanvasManager skyCanvasManager, ViewingParametersBean viewingParametersBean){
        BorderPane bottomPane = new BorderPane();
        bottomPane.setStyle("-fx-padding: 4; -fx-background-color: white;");
        StringExpression dynamicFOV = Bindings.format("Champ de vue : %.1f°", viewingParametersBean.fieldOfViewDegProperty());
        Text fieldOfView = new Text();//dynamicFOV.getValue()
        fieldOfView.textProperty().bind(dynamicFOV);

        Text objUnderMouse = new Text();
        ObjectBinding<CelestialObject> objUnderMouseBinding = skyCanvasManager.objectUnderMouseProperty();

        objUnderMouseBinding.addListener(
                (p, o, n) -> {
                    CelestialObject closestObject = objUnderMouseBinding.get();
                    if(closestObject != null)
                        objUnderMouse.setText(
                                closestObject.info());
                }
        );

        StringExpression azAltBind = Bindings.format(Locale.ROOT, "Azimut : %.1f°, hauteur : %.1f°", skyCanvasManager.mouseAzDegProperty(), skyCanvasManager.mouseAltDegProperty());
        Text azimuthAltitude = new Text();
        azimuthAltitude.textProperty().bind(azAltBind);

        bottomPane.setLeft(fieldOfView);
        bottomPane.setCenter(objUnderMouse);
        bottomPane.setRight(azimuthAltitude);

        return bottomPane;
    }

    private List<Node> timeFormatter(ObjectProperty<LocalTime> time, TimeAnimator timeAnimator){
        DateTimeFormatter hmsFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter =
                new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter =
                new TextFormatter<>(stringConverter);

        TextField textField =
                new TextField();
        textField.setTextFormatter(timeFormatter);
        textField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
        textField.disableProperty().bind(timeAnimator.runningProperty());

        timeFormatter.valueProperty().bindBidirectional(time);

        Label label = new Label("Heure :");

        return List.of(label, textField);
    }

    private List<Node> positionFormatter(String string, DoubleProperty doubleProperty, DoublePredicate predicate){
        NumberStringConverter stringConverter =
                new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newDeg =
                        stringConverter.fromString(newText).doubleValue();

                return predicate.test(newDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> textFormatter =
                new TextFormatter<>(stringConverter, 0, lonFilter);

        TextField textField =
                new TextField();
        textField.setTextFormatter(textFormatter);
        textField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        textFormatter.valueProperty().bindBidirectional(doubleProperty);

        Label label = new Label(string);

        return List.of(label, textField);
    }
}
