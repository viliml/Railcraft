/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package mods.railcraft.common.blocks.signals;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import mods.railcraft.api.signals.IControllerTile;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SimpleSignalController;
import mods.railcraft.common.util.misc.Game;

public class TileSignalBlockSignal extends TileSignalBase implements IControllerTile, ISignalTile, ISignalBlockTile {

    private final SimpleSignalController controller = new SimpleSignalController(getName(), this);
    private final SignalBlock signalBlock = new SignalBlockSimple(this);

    @Override
    public EnumSignal getSignalType() {
        return EnumSignal.BLOCK_SIGNAL;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (Game.isNotHost(worldObj)) {
            controller.tickClient();
            signalBlock.tickClient();
            return;
        }
        controller.tickServer();
        signalBlock.tickServer();
        SignalAspect prevAspect = controller.getAspect();
        if (controller.isBeingPaired()) {
            controller.setAspect(SignalAspect.BLINK_YELLOW);
        } else {
            controller.setAspect(signalBlock.getSignalAspect());
        }
        if (prevAspect != controller.getAspect()) {
            sendUpdateToClient();
        }
    }

    @Override
    public SignalAspect getSignalAspect() {
        return controller.getAspect();
    }

    @Override
    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);

        signalBlock.writeToNBT(data);
        controller.writeToNBT(data);
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        signalBlock.readFromNBT(data);
        controller.readFromNBT(data);
    }

    @Override
    public void writePacketData(DataOutputStream data) throws IOException {
        super.writePacketData(data);
        controller.writePacketData(data);
    }

    @Override
    public void readPacketData(DataInputStream data) throws IOException {
        super.readPacketData(data);
        controller.readPacketData(data);
    }

    @Override
    public SimpleSignalController getController() {
        return controller;
    }

    @Override
    public SignalBlock getSignalBlock() {
        return signalBlock;
    }
}
