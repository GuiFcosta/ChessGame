package pt.isec.pa.chess.ui.res;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;

public class ImageManager {
    private static final HashMap<String, Image> images = new HashMap<>();

    private ImageManager() {
    }

    public static Image getImage(String fileName) {
        Image image = images.get(fileName);

        if (image == null)
            try (InputStream is = ImageManager.class.getResourceAsStream("images/" + fileName)) {
                image = new Image(is);
                images.put(fileName, image);
            } catch (Exception e) {
                return null;
            }

        return image;
    }

    public static Image getExternalImage(String fileName) {
        Image image = images.get(fileName);

        if (image == null)
            try {
                image = new Image(fileName);
                images.put(fileName, image);
            } catch (Exception e) {
                return null;
            }

        return image;
    }

    public static void purgeImage(String fileName) {
        images.remove(fileName);
    }
}
