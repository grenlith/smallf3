package io.grenlith.smallf3;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = SmallF3.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SmallF3Config {
    public static final GeneralConfig GENERAL;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        GENERAL = new GeneralConfig(BUILDER);
        CLIENT_SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == CLIENT_SPEC) {
            GENERAL.bake();
        }
    }

    public static class GeneralConfig {
        public static boolean enabled;
        private final ForgeConfigSpec.BooleanValue ENABLED;

        public static String regex;
        private final ForgeConfigSpec.ConfigValue<String> REGEX;

        public static boolean allowMatching;
        private final ForgeConfigSpec.BooleanValue ALLOW_MATCHING;

        public GeneralConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("smallf3 config").push("general");

            ENABLED = builder
                    .comment("Enable the mod (true/false)")
                    .define("enabled", true);

            REGEX = builder
                    .comment("Regular expression used to match fields on the F3 screen")
                    .define("regex", ".*(fps|XYZ|Biome|Facing|Block:|Chunk:|(?<!#)minecraft:).*");

            ALLOW_MATCHING = builder
                    .comment("Use allow-list (true) or deny-list (false) for regular expression filtering")
                    .define("allowMatching", true);

            builder.pop();
        }

        public void bake() {
            enabled = ENABLED.get();
            regex = REGEX.get();
            allowMatching = ALLOW_MATCHING.get();
        }
    }
}