package com.example.addon.modules;

import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class ElytraPiglinsESP extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<Boolean> onlyPiglins = sgGeneral.add(new BoolSetting.Builder()
        .name("only-piglins")
        .description("If enabled, only highlights piglins. If disabled, highlights any living mob wearing Elytra.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("range")
        .description("Only checks entities within this range.")
        .defaultValue(128)
        .min(16)
        .sliderMax(512)
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("ESP line color.")
        .defaultValue(new SettingColor(255, 0, 255, 255))
        .build()
    );

    private final Setting<Double> lineWidth = sgRender.add(new DoubleSetting.Builder()
        .name("line-width")
        .description("ESP line width.")
        .defaultValue(1.5)
        .min(0.5)
        .sliderMax(5)
        .build()
    );

    public ElytraPiglinsESP(meteordevelopment.meteorclient.systems.modules.Category category) {
        super(category, "elytra-piglins-esp", "Highlights piglins (or any mob) wearing an Elytra.");
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (mc.world == null || mc.player == null) return;

        double r2 = range.get() * range.get();

        for (var entity : mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity le)) continue;
            if (entity == mc.player) continue;

            // Distance filter
            if (mc.player.squaredDistanceTo(entity) > r2) continue;

            // Piglin filter
            if (onlyPiglins.get() && !(entity instanceof PiglinEntity)) continue;

            // Elytra check (chest slot)
            ItemStack chest = le.getEquippedStack(EquipmentSlot.CHEST);
            if (!chest.isOf(Items.ELYTRA)) continue;

            // Draw box
            Box bb = entity.getBoundingBox();
            event.renderer.box(
                bb.minX, bb.minY, bb.minZ,
                bb.maxX, bb.maxY, bb.maxZ,
                lineColor.get(), lineColor.get(),
                (meteordevelopment.meteorclient.renderer.ShapeMode) meteordevelopment.meteorclient.renderer.ShapeMode.Lines,
                lineWidth.get().floatValue()
            );
        }
    }
}
