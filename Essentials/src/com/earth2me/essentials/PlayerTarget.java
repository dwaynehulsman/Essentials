package com.earth2me.essentials;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class PlayerTarget implements ITarget {
    private final String name;
    private final Player base;

    public PlayerTarget(Player entity) {
        this.base = entity;
        this.name = entity.getName();
    }

    @Override
    public Location getLocation() {
        return Bukkit.getServer().getPlayerExact(name).getLocation();
    }

    public Player getBase() {
        return base;
    }
}