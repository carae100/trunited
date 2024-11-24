package io.github.dracosomething.trunited.registry;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import io.github.dracosomething.trunited.ability.skill.unique.voiceofhonkai;
import io.github.dracosomething.trunited.race.honkaiapostle;
import io.github.dracosomething.trunited.trunited;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = trunited.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class skillregistry {
    public static final ResourceLocation VOICEOFHONKAI = new ResourceLocation("trunion", "voiceofhonkai");

    public skillregistry() {
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(SkillAPI.getSkillRegistry().getRegistryKey(), (helper) -> {
            helper.register("voiceofhonkai", new voiceofhonkai());
        });
    }
}
