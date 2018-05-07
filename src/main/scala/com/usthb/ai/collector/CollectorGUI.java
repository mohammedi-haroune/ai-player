package com.usthb.ai.collector;

import akka.actor.ActorRef;
import com.usthb.ai.controller.Action;
import com.usthb.ai.predictor.Gesture;
import com.usthb.ai.predictor.Input;
import com.usthb.ai.predictor.Point;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

public class CollectorGUI {
    @FXML
    private
    TextField x0, y0, z0, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, x5, y5, z5, x6, y6, z6, x7, y7, z7, x8, y8, z8, x9, y9, z9, x10, y10, z10, x11, y11, z11;

    @FXML
    TextField num;

    @FXML
    private
    TextField c;

    @FXML
    private HBox images;

    @FXML
    public ProgressIndicator fistIndicator;
    @FXML
    public ProgressIndicator stopIndicator;
    @FXML
    public ProgressIndicator point1Indicator;
    @FXML
    public ProgressIndicator point2Indicator;
    @FXML
    public ProgressIndicator grabIndicator;
    @FXML
    public Button predict;
    @FXML
    public Button send;

    private ActorRef collector;

    private Gesture gesture;

    public void setGesture(Gesture gesture) {
        this.gesture = gesture;
    }

    public CollectorGUI(ActorRef collector) {
        this.collector = collector;
    }

    public CollectorGUI() {
    }

    public void setCollector(ActorRef collector) {
        this.collector = collector;
    }

    @FXML
    void initialize() {
        send.setDisable(true);
        predict.setDisable(true);

        fistIndicator.setProgress(0);
        stopIndicator.setProgress(0);
        point1Indicator.setProgress(0);
        point2Indicator.setProgress(0);
        grabIndicator.setProgress(0);

        Light.Distant light = new Light.Distant();
        light.setColor(Color.GRAY);

        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.5);


        images.getChildren().forEach(node -> {
            if (node instanceof ImageView) {
            node.setOnMouseClicked(mouseEvent -> {
                send.setDisable(true);
                predict.setDisable(false);

                    ImageView imageView = (ImageView) node;
                    String[] path = imageView.getImage().impl_getUrl().split("/");
                    try {
                        getFromDataset(path[path.length - 1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imageView.setEffect(lighting);
                    images.getChildren().stream().filter(n -> (n instanceof ImageView) && (n != imageView)).forEach(i -> i.setEffect(null));
                });
            }
        });
    }

    @FXML
    void predict() {
        Point p0 = new Point(Double.parseDouble(x0.getText()), Double.parseDouble(y0.getText()), Double.parseDouble(z0.getText()));
        Point p1 = new Point(Double.parseDouble(x1.getText()), Double.parseDouble(y1.getText()), Double.parseDouble(z1.getText()));
        Point p2 = new Point(Double.parseDouble(x2.getText()), Double.parseDouble(y2.getText()), Double.parseDouble(z2.getText()));
        Point p3 = new Point(Double.parseDouble(x3.getText()), Double.parseDouble(y3.getText()), Double.parseDouble(z3.getText()));
        Point p4 = new Point(Double.parseDouble(x4.getText()), Double.parseDouble(y4.getText()), Double.parseDouble(z4.getText()));
        Point p5 = new Point(Double.parseDouble(x5.getText()), Double.parseDouble(y5.getText()), Double.parseDouble(z5.getText()));
        Point p6 = new Point(Double.parseDouble(x6.getText()), Double.parseDouble(y6.getText()), Double.parseDouble(z6.getText()));
        Point p7 = new Point(Double.parseDouble(x7.getText()), Double.parseDouble(y7.getText()), Double.parseDouble(z7.getText()));
        Point p8 = new Point(Double.parseDouble(x8.getText()), Double.parseDouble(y8.getText()), Double.parseDouble(z8.getText()));
        Point p9 = new Point(Double.parseDouble(x9.getText()), Double.parseDouble(y9.getText()), Double.parseDouble(z9.getText()));
        Point p10 = new Point(Double.parseDouble(x10.getText()), Double.parseDouble(y10.getText()), Double.parseDouble(z10.getText()));
        Point p11 = new Point(Double.parseDouble(x11.getText()), Double.parseDouble(y11.getText()), Double.parseDouble(z11.getText()));
        Input input = new Input(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
        System.out.println("snending input = " + input + " to + " + collector);
        collector.tell(input, null);
    }

    @FXML
    void send() {
        collector.tell(gesture, null);
    }

    void getFromDataset(String cl) throws Exception {
        int n = 0;
        switch (cl) {
            case "fist.jpg": n = 1; break;
            case "stop.jpg": n = 2; break;
            case "point1.jpg": n = 3; break;
            case "point2.jpg": n = 4; break;
            case "grab.jpg": n = 5; break;
            default: throw new Exception("can't get class " + cl);
        }
        getFromDataset(n);
    }

    void getFromDataset(int n) throws IOException {
        Optional<String> ligneOption = Files
                .lines(Paths.get("samples.csv"))
                .skip(1)
                .filter(x -> Double.parseDouble(x.split(",")[0]) == n)
                .findAny();

        if (ligneOption.isPresent()) {
            String ligne = ligneOption.get();
            System.out.println("ligne = " + ligne);
            double[] features = Arrays.stream(ligne.split(",")).mapToDouble(Double::parseDouble).toArray();

            //c.setText(features[0] + "");

            x0.setText(features[2] + "");
            y0.setText(features[3] + "");
            z0.setText(features[4] + "");

            x1.setText(features[5] + "");
            y1.setText(features[6] + "");
            z1.setText(features[7] + "");

            x2.setText(features[8] + "");
            y2.setText(features[9] + "");
            z2.setText(features[10] + "");

            x3.setText(features[11] + "");
            y3.setText(features[12] + "");
            z3.setText(features[13] + "");

            x4.setText(features[14] + "");
            y4.setText(features[15] + "");
            z4.setText(features[16] + "");

            x5.setText(features[17] + "");
            y5.setText(features[18] + "");
            z5.setText(features[19] + "");

            x6.setText(features[20] + "");
            y6.setText(features[21] + "");
            z6.setText(features[22] + "");

            x7.setText(features[23] + "");
            y7.setText(features[24] + "");
            z7.setText(features[25] + "");

            x8.setText(features[26] + "");
            y8.setText(features[27] + "");
            z8.setText(features[28] + "");

            x9.setText(features[29] + "");
            y9.setText(features[30] + "");
            z9.setText(features[31] + "");

            x10.setText(features[32] + "");
            y10.setText(features[33] + "");
            z10.setText(features[34] + "");

            x11.setText(features[35] + "");
            y11.setText(features[36] + "");
            z11.setText(features[37] + "");
        }
    }

    void updateProgressBars(double[] out) {
        fistIndicator.setProgress(out[0]);
        stopIndicator.setProgress(out[1]);
        point1Indicator.setProgress(out[2]);
        point2Indicator.setProgress(out[3]);
        grabIndicator.setProgress(out[4]);
        send.setDisable(false);
    }

    void predictorNotFound() {
        showPopup(Alert.AlertType.WARNING, "Predictor not found", "you can start it using :",  "ai-player predictor-app <python-path>");
    }

    void playerNotFound() {
        showPopup(Alert.AlertType.WARNING, "Player not found", "you can start it using :", "ai-player player-app <video-path>");
    }

    void noCommandFound() {
        showPopup(Alert.AlertType.ERROR, "No action found", "No action found for the given gesture", "you can configure one using the configuration application");
    }

    void actionExecuted(Action action) {
        showPopup(Alert.AlertType.INFORMATION, "Successful Action","Executed Action Successfully : " + action.toString());
    }

    void showPopup(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    void showPopup(Alert.AlertType type, String title, String header) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}