package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofdestruction;
import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.ability.skill.ultimate.powerofhonkai;
import io.github.dracosomething.trawakened.ability.skill.ultimate.willofhonkai;
import io.github.dracosomething.trawakened.ability.skill.unique.voiceofhonkai;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class skillregistry {
    public static final ResourceLocation VOICEOFHONKAI = new ResourceLocation("trawakened", "voiceofhonkai");
    public static final ResourceLocation POWEROFHONKAI = new ResourceLocation("trawakened", "powerofhonkai");
    public static final ResourceLocation WILLOFHONKAI = new ResourceLocation("trawakened", "willofhonkai");
    public static final ResourceLocation HERRSCHEROFDESTRUCTION = new ResourceLocation("trawakened", "herrscherofdestructionskill");
    public static final ResourceLocation HERRSCHEROFPLAGUE  = new ResourceLocation("trawakened", "herrscherofplagueskill");

    public skillregistry() {
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(SkillAPI.getSkillRegistry().getRegistryKey(), (helper) -> {
            helper.register("voiceofhonkai", new voiceofhonkai());
            helper.register("powerofhonkai", new powerofhonkai());
            helper.register("willofhonkai", new willofhonkai());
            helper.register("herrscherofdestructionskill", new herrscherofdestruction());
            helper.register("herrscherofplagueskill", new herrscherofplague());
        });
    }
}
