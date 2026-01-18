package com.example.addon;

import com.example.addon.commands.CommandExample;
import com.example.addon.hud.HudExample;
import com.example.addon.modules.ModuleExample;
import com.example.addon.modules.ElytraPiglinsESP;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class AddonTemplate extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();

    // Keep the template category + HUD group working exactly as before
    public static final Category CATEGORY = new Category("Example");
    public static final HudGroup HUD_GROUP = new HudGroup("Example");

    // New category for your Elytra module (shows as its own tab/category in Meteor)
    public static final Category ELYTRA_CATEGORY = new Category("ElytraTools");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");

        // Modules (keep existing)
        Modules.get().add(new ModuleExample());

        // Modules (add new)
        Modules.get().add(new ElytraPiglinsESP(ELYTRA_CATEGORY));

        // Commands (keep existing)
        Commands.add(new CommandExample());

        // HUD (keep existing)
        Hud.get().register(HudExample.INFO);
    }

    @Override
    public void onRegisterCategories() {
        // Keep existing category
        Modules.registerCategory(CATEGORY);

        // Register new category so it shows up in Meteor
        Modules.registerCategory(ELYTRA_CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.example.addon";
    }

    @Override
    public GithubRepo getRepo() {
        // Optional: change these to YOUR fork owner/name if you want
        // return new GithubRepo("YourGitHubName", "your-fork-repo");
        return new GithubRepo("MeteorDevelopment", "meteor-addon-template");
    }
}
