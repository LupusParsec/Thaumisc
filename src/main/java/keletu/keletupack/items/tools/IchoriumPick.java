package keletu.keletupack.items.tools;

import keletu.keletupack.init.ModItems;
import keletu.keletupack.keletupack;
import keletu.keletupack.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class IchoriumPick extends ItemPickaxe implements IHasModel {
    public IchoriumPick(String name, CreativeTabs tab, ToolMaterial material) {

        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(tab);

        ModItems.ITEMS.add(this);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.RARE;
    }
    @Override
    public void registerModels() {
        keletupack.proxy.registerItemRenderer(this, 0, "inventory");
    }
}