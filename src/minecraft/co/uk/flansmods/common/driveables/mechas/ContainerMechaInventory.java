package co.uk.flansmods.common.driveables.mechas;

import co.uk.flansmods.common.driveables.EntityDriveable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerMechaInventory extends Container 
{
	public InventoryPlayer inventory;
    public World world;
	public EntityMecha mecha;
	public int numItems;
	public int maxScroll;
	public int scroll;
	
	public ContainerMechaInventory(InventoryPlayer inv, World w, EntityMecha em)
	{
		inventory = inv;
		world = w;
		mecha = em;
		numItems = mecha.getDriveableType().numCargoSlots;
		int numRows = (int)((numItems + 7) / 8);
		maxScroll = (numRows > 3 ? numRows - 3 : 0);
		
		int startSlot = mecha.driveableData.getCargoInventoryStart();

		for(int row = 0; row < numRows; row++)
		{
			int yPos = -1000;
			if(row < 3 + scroll && row >= scroll)
				yPos = 25 + 19 * (row - scroll);
			for(int col = 0; col < ((row + scroll + 1) * 8 < numItems ? 8 : numItems % 8); col++)
			{
				addSlotToContainer(new Slot(mecha.driveableData, startSlot + row * 8 + col, 10 + 18 * col, yPos));
			}
		}
		
		//Main inventory slots
        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 9; col++)
            {
            	addSlotToContainer(new Slot(inventory, col + row * 9 + 9, 182 + col * 18, 98 + row * 18));
            }

        }
		//Quickbar slots
        for(int col = 0; col < 9; col++)
        {
        	addSlotToContainer(new Slot(inventory, col, 182 + col * 18, 156));
        }
	}

	public void updateScroll(int scrololol)
	{
		scroll = scrololol;
				int m = (int)((numItems + 7) / 8);
				for(int row = 0; row < m; row++)
				{
					int yPos = -1000;
					if(row < 3 + scroll && row >= scroll)
						yPos = 25 + 19 * (row - scroll);
					for(int col = 0; col < ((row + 1) * 8 < numItems ? 8 : numItems % 8); col++)
					{
						((Slot)inventorySlots.get(row * 8 + col)).yDisplayPosition = yPos;
					}
				}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) 
	{
		return true;
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
    {
		ItemStack stack = null;
        Slot currentSlot = (Slot)inventorySlots.get(slotID);

        if(currentSlot != null && currentSlot.getHasStack())
        {
            ItemStack slotStack = currentSlot.getStack();
            stack = slotStack.copy();
            
            if(slotID >= numItems)
            {
            	if(!mergeItemStack(slotStack, 0, numItems, false))
            	{
            		return null;
            	}
            }
            else {
            	if(!mergeItemStack(slotStack, numItems, inventorySlots.size(), true))
            	{
            		return null;
            	}
            }
            
            if (slotStack.stackSize == 0)
            {
                currentSlot.putStack((ItemStack)null);
            }
            else
            {
                currentSlot.onSlotChanged();
            }

            if (slotStack.stackSize == stack.stackSize)
            {
                return null;
            }

            currentSlot.onPickupFromSlot(player, slotStack);
        }

        return stack;
    }

}
