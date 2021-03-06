package com.freedsuniverse.sidecraft.entity;

import java.awt.Rectangle;

import com.freedsuniverse.sidecraft.Engine;
import com.freedsuniverse.sidecraft.Settings;
import com.freedsuniverse.sidecraft.Sidecraft;
import com.freedsuniverse.sidecraft.material.Material;
import com.freedsuniverse.sidecraft.material.MaterialStack;
import com.freedsuniverse.sidecraft.world.Block;
import com.freedsuniverse.sidecraft.world.Location;
import com.freedsuniverse.sidecraft.world.World;

public class DropEntity implements Entity{

    private final Material type;
    private Location location;

    private int WIDTH = 15;
    private int HEIGHT = 15;

    public DropEntity(Material t, Location loc) {
        HEIGHT = Settings.BLOCK_SIZE / 2;
        WIDTH = Settings.BLOCK_SIZE / 2;
        Location newLoc = new Location(loc.getX(), loc.getY(), loc.getWorld().getName());
        spawn(newLoc);
        type = t;
    }

    public DropEntity(Material t, double x, double y, World world) {
        location = new Location(x, y, world.getName());
        type = t; HEIGHT = Settings.BLOCK_SIZE / 2;
        WIDTH = Settings.BLOCK_SIZE / 2;
        spawn(location);
    }

    public DropEntity(Material t, double x, double y) {
        HEIGHT = Settings.BLOCK_SIZE / 2;
        WIDTH = Settings.BLOCK_SIZE / 2;
        type = t;
        Location location = new Location(x, y);
        spawn(location);
    }

    public void spawn(Location loc) {
        this.location = loc;
        while (this.location.getWorld().getBlockAt(loc.modify(0, -0.5)).getType().isSolid()) {
            loc.modifyY(1);
        }
       
        loc.getWorld().registerEntity(this);
    }

    @Override
    public void update() {
        
        
        Block nextBlock = this.getLocation().getWorld().getBlockAt(new Location(location.getX(), location.getY() - 0.5));
        if (!nextBlock.getType().isSolid()) {
            location.modifyY(-0.1);
        }

        if (Math.abs(Sidecraft.player.coordinates.getX() - getLocation().getX()) <= 1.5 && Math.abs(Sidecraft.player.coordinates.getY() - getLocation().getY()) <= 1.5) {
            if (Sidecraft.player.coordinates.getX() > getLocation().getX()) {
                location.modifyX(0.2);
            }
            else {
                location.modifyX(-0.2);
            }
        }

        if(getBounds().intersects(Sidecraft.player.getBounds())){
            destroy();
        }
    }

    public void destroy() {
        Sidecraft.player.getInventory().add(new MaterialStack(this.type, 1));
        getLocation().getWorld().unregisterEntity(this);
    }

    public void draw() {
        Engine.render(location, HEIGHT, WIDTH, type.getImage());
    }

    public Location getLocation() {
        return this.location;
    }

    public Rectangle getBounds() {
        return location.toRectangle(WIDTH, HEIGHT);
    }
}
