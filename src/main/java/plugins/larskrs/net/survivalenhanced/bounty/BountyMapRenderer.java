package plugins.larskrs.net.survivalenhanced.bounty;

import org.bukkit.entity.Player;
import org.bukkit.map.*;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class BountyMapRenderer extends MapRenderer {

    private String target;

    public BountyMapRenderer (String target) {
        this.target = target;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {

        try {
            URL backgroundUrl = new URL("http://aktuelt.tv/api/v1/files?fileId=83fffe81-9a1f-487c-88a7-b00b513859a3.png");
            Messanger.InfoConsole(backgroundUrl.toString());
            BufferedImage backgroundImage = ImageIO.read(backgroundUrl);
            mapCanvas.drawImage(0,0,MapPalette.resizeImage(backgroundImage));

            URL url = new URL("https://mc-heads.net/head/" + target + "/25");
            Messanger.InfoConsole(url.toString());
            BufferedImage image = ImageIO.read(url);
            mapCanvas.drawImage(32,32, image);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mapCanvas.drawText(25, 92, MinecraftFont.Font, "Wanted");
    }
}
