package keletu.keletupack.items.armor;

import com.google.common.collect.Multimap;
import keletu.keletupack.init.ModItems;
import keletu.keletupack.keletupack;
import keletu.keletupack.util.IHasModel;
import keletu.keletupack.util.Reference;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IVisDiscountGear;

import java.util.UUID;

public class KamiArmor extends ItemArmor implements IVisDiscountGear, IGoggles, IHasModel {
    public static final ArmorMaterial ICHORADV = EnumHelper.addArmorMaterial("ICHORADV", "ichoradv", 0, new int[]{
            3,
            8,
            6,
            3
    }, 40, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 3F);
    private final int[] discounts = new int[]{
            0,
            0,
            4,
            4,
            4,
            4
    };

    public KamiArmor(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, CreativeTabs tab) {
        super(materialIn, renderIndexIn, equipmentSlotIn);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(tab);

        ModItems.ITEMS.add(this);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer mp, ItemStack itemStack) {
        ItemStack tmp;
        boolean headWorn = !(tmp = mp.getItemStackFromSlot(EntityEquipmentSlot.HEAD)).isEmpty() && tmp.getItem() instanceof KamiArmor;
        boolean bodyWorn = !(tmp = mp.getItemStackFromSlot(EntityEquipmentSlot.CHEST)).isEmpty() && tmp.getItem() instanceof KamiArmor;
        boolean beltWorn = !(tmp = mp.getItemStackFromSlot(EntityEquipmentSlot.LEGS)).isEmpty() && tmp.getItem() instanceof KamiArmor;
        boolean bootsWorn = !(tmp = mp.getItemStackFromSlot(EntityEquipmentSlot.FEET)).isEmpty() && tmp.getItem() instanceof KamiArmor;

        switch (armorType) {
            case HEAD: {
                if (mp.isInWater() && mp.ticksExisted % 10 == 0)
                    mp.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 31, 0, true, false));
                if (mp.isInLava() && mp.ticksExisted % 10 == 0)
                    mp.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 31, 0, true, false));
                int food = mp.getFoodStats().getFoodLevel();
                if (food > 0 && food < 18 && mp.shouldHeal()
                        && mp.ticksExisted % 80 == 0)
                    mp.heal(1F);
            }
            break;

            case CHEST: {
                mp.getEntityData().setBoolean("can_fly", true);
            }
            break;
            case LEGS: {
                if (mp.getActivePotionEffect(MobEffects.FIRE_RESISTANCE) == null || mp.getActivePotionEffect(MobEffects.FIRE_RESISTANCE).getDuration() <= 1) {
                    mp.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 200, 0, false, false));
                    if (mp.isBurning()) {
                        mp.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 20, 1, true, false));
                        mp.extinguish();
                    }
                }
            }
            break;

            case FEET: {
                {
                    if (!mp.capabilities.isFlying && mp.moveForward > 0.0f) {
                        if (mp.world.isRemote && !mp.isSneaking()) {
                            mp.stepHeight = 1.0f;
                        }
                        if (mp.onGround) {
                            float bonus = 0.06f;
                            if (mp.isInWater())
                                bonus /= 2.0f;
                            mp.moveRelative(0.0f, 0.0f, bonus, 1.0f);
                        } else {
                            if (mp.isInWater())
                                mp.moveRelative(0.0f, 0.0f, 0.03f, 1.0f);
                            mp.jumpMovementFactor = 0.05f;
                        }
                    }
                }
                if (mp.getActivePotionEffect(MobEffects.HASTE) == null || mp.getActivePotionEffect(MobEffects.HASTE).getDuration() <= 1) {
                    mp.addPotionEffect(new PotionEffect(MobEffects.HASTE, 200, 1, false, false));
                }
            }
            break;

        }
    }
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
        if(slot == armorType)
        {
            if(slot == EntityEquipmentSlot.LEGS)
            {
            } else if(slot == EntityEquipmentSlot.FEET)
            {

            }
        }
        return map;
    }

    @Override
    public KamiArmor setTranslationKey(String key) {
        return (KamiArmor) super.setTranslationKey(key);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return Reference.MOD_ID + ":textures/models/armor/kami_layer_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return null;
    }
    @Override
    public int getVisDiscount(ItemStack stack, EntityPlayer player) {
        return discounts[armorType.ordinal()];
    }

    @Override
    public void registerModels() {
        keletupack.proxy.registerItemRenderer(this, 0, "inventory");
    }

    @Override
    public boolean showIngamePopups(ItemStack stack, EntityLivingBase owner) {
        return armorType == EntityEquipmentSlot.HEAD;
    }
}