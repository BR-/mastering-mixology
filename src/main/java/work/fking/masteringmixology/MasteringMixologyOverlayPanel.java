package work.fking.masteringmixology;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.google.inject.Inject;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

public class MasteringMixologyOverlayPanel extends OverlayPanel {
    private final MasteringMixologyPlugin plugin;

    @Inject
    private MasteringMixologyOverlayPanel(MasteringMixologyPlugin plugin) {
        super(plugin);
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (plugin.isInLab()) {
            // panelComponent.getChildren().add(
            //     TitleComponent.builder()
            //         .text("Mixology")
            //         .build());
            int ndx = 0;
            for (PotionOrder o : plugin.getPotionOrderSequence()) {
                String prefix = ndx == plugin.getMixIndex() ? "X " : "";
                panelComponent.getChildren().add(
                    LineComponent.builder()
                        .right(prefix + o.potionType().recipe().replaceAll("</col>", ""))
                        .build()
                );
                ndx++;
            }
            ndx = 0;
            for (PotionOrder o : plugin.getPotionOrderSequence()) {
                String prefix = plugin.getMixIndex() >= plugin.getPotionOrderSequence().size() && ndx == plugin.getModifyIndex() ? "X " : "";
                panelComponent.getChildren().add(
                    LineComponent.builder()
                        .right(prefix + o.potionModifier().alchemyObject().toString())
                        .build()
                );
                ndx++;
            }
        }
        panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth("XXAGITATORXXX"), 0));
        return super.render(graphics);
    }
}
