package com.example.imageeditor;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ImageEditorController {

    @FXML
    private ImageView imageView;

    @FXML
    private CheckBox greyscaleCheckBox; // Link to FXML
    //stores og image
    private Image originalImage;

    @FXML
    public void handleExit() {
        System.exit(0);
    }

    @FXML
    public void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            originalImage = new Image(file.toURI().toString());
            imageView.setImage(originalImage);

            // Reset the checkbox state when a new image is loaded
            greyscaleCheckBox.setSelected(false);
        }
    }

    @FXML
    public void handleGreyscaleToggle() {
        if (originalImage == null) return;

        if (greyscaleCheckBox.isSelected()) {
            imageView.setImage(applyGreyscale(originalImage));
        } else {
            imageView.setImage(originalImage);
        }
    }

    private Image applyGreyscale(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage greyscaleImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = greyscaleImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double greyValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
                Color greyColor = new Color(greyValue, greyValue, greyValue, color.getOpacity());
                pixelWriter.setColor(x, y, greyColor);
            }
        }

        return greyscaleImage;
    }
}
