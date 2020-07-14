package me.jackint0sh.timedfly.versions.v1_16;

import me.jackint0sh.timedfly.versions.ServerVersion;
import net.minecraft.server.v1_16_R1.ItemStack;
import net.minecraft.server.v1_16_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;

public class v1_16_R1 extends ServerVersion {

    public final static ServerVersion v1_16 = new v1_16_R1();

    @Override
    public org.bukkit.inventory.ItemStack setNBT(org.bukkit.inventory.ItemStack itemStack, String key, String value) {
        ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();

        tag.setString(key, value);
        stack.setTag(tag);

        return CraftItemStack.asCraftMirror(stack);
    }

    @Override
    public boolean hasTag(org.bukkit.inventory.ItemStack itemStack, String key) {
        ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = stack.getTag();

        if (tag == null) return false;
        return !tag.getString(key).isEmpty();
    }

    @Override
    public String getTag(org.bukkit.inventory.ItemStack itemStack, String key) {
        ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = stack.getTag();
        if (tag == null) return null;
        return tag.getString(key);
    }
}
