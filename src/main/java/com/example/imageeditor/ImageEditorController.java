package com.example.imageeditor;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
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
    private CheckBox greyscaleCheckBox;

    @FXML
    private Slider redSlider;

    @FXML
    private Slider greenSlider;

    @FXML
    private Slider blueSlider;

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

        Stage stage = (Stage) imageView.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            originalImage = new Image(file.toURI().toString());
            imageView.setImage(originalImage);
            greyscaleCheckBox.setSelected(false); // Reset greyscale checkbox
        }
    }

    @FXML
    public void handleGreyscaleToggle() {
        if (originalImage == null) return;
        imageView.setImage(greyscaleCheckBox.isSelected() ? applyGreyscale(originalImage) : originalImage);
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

    @FXML
    private void changeColorChannels() {
        if (originalImage == null) return;
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();
        WritableImage adjustedImage = new WritableImage(width, height);
        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = adjustedImage.getPixelWriter();

        double redScale = redSlider.getValue();
        double greenScale = greenSlider.getValue();
        double blueScale = blueSlider.getValue();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double newRed = Math.min(color.getRed() * redScale, 1.0);
                double newGreen = Math.min(color.getGreen() * greenScale, 1.0);
                double newBlue = Math.min(color.getBlue() * blueScale, 1.0);
                Color newColor = new Color(newRed, newGreen, newBlue, color.getOpacity());
                pixelWriter.setColor(x, y, newColor);
            }
        }

        imageView.setImage(adjustedImage);
    }
}
