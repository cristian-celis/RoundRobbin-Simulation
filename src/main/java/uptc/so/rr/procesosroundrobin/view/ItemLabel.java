package uptc.so.rr.procesosroundrobin.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * @implNote This class is used to create a customized visual label component for a text
 */
public class ItemLabel extends Label {

    public ItemLabel(String text) {
        setText(text);
        setFont(Font.font("Arial", 12));
        setAlignment(Pos.CENTER);
        setMaxWidth(Double.MAX_VALUE);
        setPrefHeight(USE_COMPUTED_SIZE);
        setStyle("-fx-border-color: gray gray gray gray;");
    }

}
