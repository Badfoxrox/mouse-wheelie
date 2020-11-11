package de.siphalor.mousewheelie.client.keybinding;

import de.siphalor.amecs.api.AmecsKeyBinding;
import de.siphalor.amecs.api.KeyModifiers;
import de.siphalor.amecs.api.PriorityKeyBinding;
import de.siphalor.mousewheelie.client.inventory.ToolPicker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class PickToolKeyBinding extends AmecsKeyBinding implements PriorityKeyBinding {
	public PickToolKeyBinding(Identifier id, InputUtil.Type type, int code, String category, KeyModifiers defaultModifiers) {
		super(id, type, code, category, defaultModifiers);
	}

	@Override
	public boolean onPressedPriority() {
		if (MinecraftClient.getInstance().currentScreen != null) return false;
		PlayerEntity playerEntity = MinecraftClient.getInstance().player;
		if (playerEntity != null) {
			HitResult hitResult = playerEntity.raycast(4.5D, 0.0F, false);
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				return new ToolPicker(playerEntity.getInventory()).pickToolFor(playerEntity.world.getBlockState(((BlockHitResult) hitResult).getBlockPos()));
			} else {
				return new ToolPicker(playerEntity.getInventory()).pickWeapon();
			}
		}
		return false;
	}
}
