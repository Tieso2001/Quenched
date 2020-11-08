package net.tieso2001.quenched.init;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tieso2001.quenched.Quenched;
import net.tieso2001.quenched.potion.DehydrationEffect;

public class ModEffects {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Quenched.MOD_ID);

    public static final RegistryObject<Effect> DEHYDRATION = EFFECTS.register("dehydration", DehydrationEffect::new);
}
