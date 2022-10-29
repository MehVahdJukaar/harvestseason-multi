package net.mehvahdjukaar.harvestseason.network;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.NetworkDir;

public class NetworkHandler {

    public static ChannelHandler CHANNEL;


    public static void registerMessages() {

        CHANNEL = ChannelHandler.createChannel(HarvestSeason.res("network"));

        CHANNEL.register(NetworkDir.PLAY_TO_SERVER,
                ServerBoundCarvePumpkinPacket.class, ServerBoundCarvePumpkinPacket::new);


    }

}