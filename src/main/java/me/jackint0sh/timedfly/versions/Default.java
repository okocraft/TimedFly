package me.jackint0sh.timedfly.versions;

import org.bukkit.inventory.ItemStack;

public class Default extends ServerVersion {

    public Default() {
        //serverVersion = this;
    }

    @Override
    public ItemStack setNBT(ItemStack itemStack, String key, String value) {
        return itemStack;
    }

    @Override
    public boolean hasTag(ItemStack itemStack, String key) {
        return false;
    }

    @Override
    public String getTag(ItemStack itemStack, String key) {
        return null;
    }
}
