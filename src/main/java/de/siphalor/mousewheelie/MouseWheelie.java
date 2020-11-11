package de.siphalor.mousewheelie;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;

public class MouseWheelie implements ModInitializer {
	public static final String MOD_ID = "mousewheelie";

	@Override
	public void onInitialize() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack stack = player.getStackInHand(hand);
			if (!world.isClient()) {
				EquipmentSlot equipmentSlot = MobEntity.method_32326(stack);
				if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR) {
					ItemStack equipmentStack = player.getEquippedStack(equipmentSlot);
					if (!equipmentStack.isEmpty()) {
						player.setStackInHand(hand, equipmentStack);
						player.equipStack(equipmentSlot, stack);
						return TypedActionResult.consume(equipmentStack);
					}
				}
			}
			return TypedActionResult.pass(stack);
		});
	}
}
