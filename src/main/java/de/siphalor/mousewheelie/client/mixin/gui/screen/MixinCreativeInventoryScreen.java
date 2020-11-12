package de.siphalor.mousewheelie.client.mixin.gui.screen;

import de.siphalor.mousewheelie.MWConfig;
import de.siphalor.mousewheelie.client.compat.FabricCreativeGuiHelper;
import de.siphalor.mousewheelie.client.inventory.CreativeContainerScreenHelper;
import de.siphalor.mousewheelie.client.util.ScrollAction;
import de.siphalor.mousewheelie.client.util.accessors.IContainerScreen;
import de.siphalor.mousewheelie.client.util.accessors.ISlot;
import de.siphalor.mousewheelie.client.util.accessors.ISpecialScrollableScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreativeInventoryScreen.class)
public abstract class MixinCreativeInventoryScreen extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeContainer> implements ISpecialScrollableScreen, IContainerScreen {

	@Shadow
	private static int selectedTab;

	@Shadow
	protected abstract void setSelectedTab(ItemGroup itemGroup_1);

	@Shadow
	protected abstract void onMouseClick(Slot slot, int invSlot, int button, SlotActionType slotActionType);

	public MixinCreativeInventoryScreen(CreativeInventoryScreen.CreativeContainer container_1, PlayerInventory playerInventory_1, Text textComponent_1) {
		super(container_1, playerInventory_1, textComponent_1);
	}

	@Override
	public ScrollAction mouseWheelie_onMouseScrolledSpecial(double mouseX, double mouseY, double scrollAmount) {
		boolean yOverTopTabs = (this.y - 32 <= mouseY && mouseY <= this.y);
		boolean yOverBottomTabs = (this.y + this.backgroundHeight <= mouseY && mouseY <= this.y + this.backgroundHeight + 32);
		boolean overTabs = (this.x <= mouseX && mouseX <= this.x + this.backgroundWidth) && (yOverTopTabs || yOverBottomTabs);

		if (overTabs) {
			if (FabricLoader.getInstance().isModLoaded("fabric") || FabricLoader.getInstance().isModLoaded("fabric-item-groups")) {
				FabricCreativeGuiHelper helper = new FabricCreativeGuiHelper((CreativeInventoryScreen) (Object) this);
				int newIndex = MathHelper.clamp(selectedTab + (int) Math.round(scrollAmount * MWConfig.scrolling.scrollFactor), 0, ItemGroup.GROUPS.length - 1);
				int newPage = helper.getPageForTabIndex(newIndex);
				if (newPage < helper.getCurrentPage())
					helper.previousPage();
				if (newPage > helper.getCurrentPage())
					helper.nextPage();
				setSelectedTab(ItemGroup.GROUPS[newIndex]);
			} else {
				setSelectedTab(ItemGroup.GROUPS[MathHelper.clamp((int) (selectedTab + Math.round(scrollAmount * MWConfig.scrolling.scrollFactor)), 0, ItemGroup.GROUPS.length - 1)]);
			}
			return ScrollAction.SUCCESS;
		}

		if (MWConfig.scrolling.enable && selectedTab != ItemGroup.INVENTORY.getIndex()) {
			if (MWConfig.scrolling.scrollCreativeMenu == !hasAltDown())
				return ScrollAction.ABORT;
			Slot hoverSlot = this.mouseWheelie_getSlotAt(mouseX, mouseY);
			if (hoverSlot != null) {
				new CreativeContainerScreenHelper<>((CreativeInventoryScreen) (Object) this, (slot, data, slotActionType) -> onMouseClick(slot, ((ISlot) slot).mouseWheelie_getInvSlot(), data, slotActionType)).scroll(hoverSlot, scrollAmount < 0);
				return ScrollAction.SUCCESS;
			}
		}

		return ScrollAction.PASS;
	}
}
