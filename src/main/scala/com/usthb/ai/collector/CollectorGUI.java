package com.usthb.ai.collector;

import akka.actor.ActorRef;
import com.usthb.ai.player.Input;
import com.usthb.ai.player.Point;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

public class CollectorGUI {
    @FXML
    private
    TextField x0;
    @FXML
    private
    TextField y0;
    @FXML
    private
    TextField z0;
    @FXML
    private
    TextField x1;
    @FXML
    private
    TextField y1;
    @FXML
    private
    TextField z1;
    @FXML
    private
    TextField x2;
    @FXML
    private
    TextField y2;
    @FXML
    private
    TextField z2;
    @FXML
    private
    TextField x3;
    @FXML
    private
    TextField y3;
    @FXML
    private
    TextField z3;
    @FXML
    private
    TextField x4;
    @FXML
    private
    TextField y4;
    @FXML
    private
    TextField z4;
    @FXML
    private
    TextField x5;
    @FXML
    private
    TextField y5;
    @FXML
    private
    TextField z5;
    @FXML
    private
    TextField x6;
    @FXML
    private
    TextField y6;
    @FXML
    private
    TextField z6;
    @FXML
    private
    TextField x7;
    @FXML
    private
    TextField y7;
    @FXML
    private
    TextField z7;
    @FXML
    private
    TextField x8;
    @FXML
    private
    TextField y8;
    @FXML
    private
    TextField z8;
    @FXML
    private
    TextField x9;
    @FXML
    private
    TextField y9;
    @FXML
    private
    TextField z9;
    @FXML
    private
    TextField x10;
    @FXML
    private
    TextField y10;
    @FXML
    private
    TextField z10;
    @FXML
    private
    TextField x11;
    @FXML
    private
    TextField y11;
    @FXML
    private
    TextField z11;

    @FXML
    TextField num;

    @FXML
    private
    TextField c;

    private ActorRef predictor;

    public CollectorGUI(ActorRef predictor) {
        this.predictor = predictor;
    }

    @FXML
    void send() {
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
        predictor.tell(input, null);
    }

    @FXML
    void getFromDatset() throws IOException {
        String path = "cleaned.csv";
        int n = Integer.parseInt(c.getText());
        Optional<String> ligneOption = Files.lines(Paths.get(path)).skip(1).filter(x -> Double.parseDouble(x.split(",")[0]) == n).findFirst();
        if (ligneOption.isPresent()) {
            String ligne = ligneOption.get();
            System.out.println("ligne = " + ligne);
            double[] features = Arrays.stream(ligne.split(",")).mapToDouble(Double::parseDouble).toArray();

            c.setText(features[0] + "");

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
}