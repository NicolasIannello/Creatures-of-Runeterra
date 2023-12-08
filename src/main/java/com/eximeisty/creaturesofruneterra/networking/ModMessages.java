package com.eximeisty.creaturesofruneterra.networking;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.networking.packet.C2SDunebreakerShield;
import com.eximeisty.creaturesofruneterra.networking.packet.C2SFiddleArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0").clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true).simpleChannel();

        INSTANCE = net;

        net.messageBuilder(C2SFiddleArmor.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SFiddleArmor::new).encoder(C2SFiddleArmor::toBytes)
                .consumerMainThread(C2SFiddleArmor::handle).add();

        net.messageBuilder(C2SDunebreakerShield.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SDunebreakerShield::new).encoder(C2SDunebreakerShield::toBytes)
                .consumerMainThread(C2SDunebreakerShield::handle).add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

//    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
//        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
//    }
}
