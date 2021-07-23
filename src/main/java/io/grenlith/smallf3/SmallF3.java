package io.grenlith.smallf3;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.text.StyleContext;
import java.util.ListIterator;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SmallF3.MOD_ID)
public class SmallF3 {

    public static final String MOD_ID = "smallf3";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static Supplier<Minecraft> minecraftSupplier;

    public SmallF3() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SmallF3Config.CLIENT_SPEC);
    }

    private void setup(final FMLClientSetupEvent event)
    {
        minecraftSupplier = event.getMinecraftSupplier();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if(!SmallF3Config.GeneralConfig.enabled) {
            return;
        }

        Pattern p = null;
        try {
            p = Pattern.compile(SmallF3Config.GeneralConfig.regex);
        } catch (PatternSyntaxException ex) {
            event.getRight().add("");
            event.getRight().add("smallf3:");
            event.getRight().add(ex.getDescription() + " @ " + ex.getIndex());
        }

        ListIterator<String> leftLines = event.getLeft().listIterator();
        ListIterator<String> rightLines = event.getRight().listIterator();
        while(leftLines.hasNext()) {
            if(!lineMatches(p, leftLines.next()))
                leftLines.remove();
        }
        while(rightLines.hasNext()) {
            if(!lineMatches(p, rightLines.next()))
                rightLines.remove();
        }
    }

    private static Boolean lineMatches(Pattern pattern, String line) {
        if(pattern.matcher(line).matches()) {
            return SmallF3Config.GeneralConfig.allowMatching;
        }
        return !SmallF3Config.GeneralConfig.allowMatching;
    }
}
