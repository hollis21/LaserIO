package com.direwolf20.laserio.common.network.packets;

import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.util.ParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PacketNodeParticles {
    private List<ParticleData> particleList;

    public PacketNodeParticles(List<ParticleData> particleList) {
        this.particleList = particleList;
    }

    public static void encode(PacketNodeParticles msg, FriendlyByteBuf buffer) {
        List<ParticleData> tempList = msg.particleList;
        int size = tempList.size();
        buffer.writeInt(size);
        for (ParticleData data : tempList) {
            buffer.writeInt(data.item);
            buffer.writeByte(data.itemCount);
            buffer.writeBlockPos(data.fromNode);
            buffer.writeByte(data.fromDirection);
            buffer.writeBlockPos(data.toNode);
            buffer.writeByte(data.toDirection);
            buffer.writeByte(data.extractPosition);
            buffer.writeByte(data.insertPosition);
        }
    }

    public static PacketNodeParticles decode(FriendlyByteBuf buffer) {
        List<ParticleData> thisList = new ArrayList<>();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            int item = buffer.readInt();
            byte itemCount = buffer.readByte();
            BlockPos fromNode = buffer.readBlockPos();
            byte fromDirection = buffer.readByte();
            BlockPos toNode = buffer.readBlockPos();
            byte toDirection = buffer.readByte();
            byte extractPosition = buffer.readByte();
            byte insertPosition = buffer.readByte();
            ParticleData data = new ParticleData(item, itemCount, fromNode, fromDirection, toNode, toDirection, extractPosition, insertPosition);
            thisList.add(data);
        }
        return new PacketNodeParticles(thisList);
    }

    public static class Handler {
        public static void handle(PacketNodeParticles msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> clientPacketHandler(msg)));
            ctx.get().setPacketHandled(true);
        }
    }

    public static void clientPacketHandler(PacketNodeParticles msg) {
        List<ParticleData> tempList = msg.particleList;

        for (ParticleData data : tempList) {
            BlockPos fromPos = data.fromNode;
            BlockEntity clientTE = Minecraft.getInstance().level.getBlockEntity(fromPos);
            if (!(clientTE instanceof LaserNodeBE)) return;
            ((LaserNodeBE) clientTE).addParticleData(data);
        }
    }
}