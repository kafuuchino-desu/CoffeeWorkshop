package net.langball.coffee.drinks;

import java.util.List;

import net.langball.coffee.effect.PotionLoader;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.thirst.ThirstHelper;

@Interface(iface="toughasnails.api.thirst.IDrink", modid="toughasnails")
public class DrinkCoffeeTest extends ItemFood {
	public PotionEffect effect;
	public DrinkCoffeeTest(int amount, float saturation,PotionEffect potion) {
		super(amount, saturation,false);
		effect=potion;
		this.setMaxStackSize(1);
	}
	
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (playerIn.canEat(true)||playerIn.isCreative())
        {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }
	 public EnumAction getItemUseAction(ItemStack stack)
	    {
	        return EnumAction.DRINK;
	    }
	 public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	    {
		 if (entityLiving instanceof EntityPlayer)
	        {
	            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
	            entityplayer.getFoodStats().addStats(this, stack);
	            worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
	            this.onFoodEaten(stack, worldIn, entityplayer);
	            entityplayer.addStat(StatList.getObjectUseStats(this));
	            if (entityplayer instanceof EntityPlayerMP)
	            {
	                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
	            }
	        }
		 return new ItemStack(DrinksLoader.cup);
	    }
	 @Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		 tooltip.add(I18n.format("word."+this.getUnlocalizedName().substring(5)+".name", new Object()));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		PotionEffect bitter_1 = new PotionEffect(PotionLoader.relax,1000,0);
		player.addPotionEffect(bitter_1);
		PotionEffect bitter_2 = new PotionEffect(Potion.getPotionById(11),1000,1);
		player.addPotionEffect(bitter_2);
		PotionEffect bitter_3 = new PotionEffect(Potion.getPotionById(1),1000,1);
		player.addPotionEffect(bitter_3);
		if(effect!=null){
		player.addPotionEffect(effect);
		}
	}
	  @Method(modid="toughasnails")
	  public void drink(EntityLivingBase entity)
	  {
	    EntityPlayer player = (EntityPlayer)entity;
	    IThirst thirst = ThirstHelper.getThirstData(player);
	    
	    thirst.addStats(getThirst(), getHydration());
	  }
	  
	  @Method(modid="toughasnails")
	  public int getThirst()
	  {
	    return 8;
	  }
	  
	  @Method(modid="toughasnails")
	  public float getHydration()
	  {
	    return 0.6F;
	  }
	  
	  @Method(modid="toughasnails")
	  public float getPoisonChance()
	  {
	    return 0.0F;
	  }
	  @Method(modid="toughasnails")
	  public void changeTemperature(EntityLivingBase entity)
	  {
	    EntityPlayer player = (EntityPlayer)entity;
	    ITemperature temperature = TemperatureHelper.getTemperatureData(player);
	    if (temperature.getTemperature().getRawValue() <= 10) {
	      temperature.setTemperature(new Temperature(temperature.getTemperature().getRawValue() + 1));
	    } else if (temperature.getTemperature().getRawValue() >= 14) {
	      temperature.setTemperature(new Temperature(temperature.getTemperature().getRawValue() - 1));
	    }
	  }
}