package de.siphalor.mousewheelie.client.inventory.sort;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.List;

/**
 * Additional context for executing a sort.
 *
 * @see SortMode#sort(int[], ItemStack[], SortContext)
 */
public class SortContext {
	private final HandledScreen<?> screen;
	private final List<Slot> relevantSlots;

	public SortContext(HandledScreen<?> screen, List<Slot> relevantSlots) {
		this.screen = screen;
		this.relevantSlots = relevantSlots;
	}

	/**
	 * Gets the screen that is currently sorted on.
	 * @return The screen
	 */
	public HandledScreen<?> getScreen() {
		return screen;
	}

	/**
	 * Gets the slots that are the target of the current sort action.
	 * These slots are usually in the same scope (see {@link de.siphalor.mousewheelie.client.inventory.ContainerScreenHelper#getScope(Slot)}).
	 * @return The relevant slots
	 */
	public List<Slot> getRelevantSlots() {
		return relevantSlots;
	}
}