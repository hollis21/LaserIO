package com.direwolf20.laserio.setup;

import com.direwolf20.laserio.common.network.PacketHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {
    public static void init(final FMLCommonSetupEvent event) {
        PacketHandler.register();
    }

    public static final String TAB_NAME = "LaserIO";
    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(TAB_NAME) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.Laser_Wrench.get());
        }
    };
}
