package mcvmcomputers.entities;

import mcvmcomputers.gui.GuiFocus;
import mcvmcomputers.item.ItemList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFlatScreen extends Entity{
	private static final TrackedData<Float> LOOK_AT_POS_X =
			DataTracker.registerData(EntityFlatScreen.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> LOOK_AT_POS_Y =
			DataTracker.registerData(EntityFlatScreen.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> LOOK_AT_POS_Z =
			DataTracker.registerData(EntityFlatScreen.class, TrackedDataHandlerRegistry.FLOAT);
	
	public EntityFlatScreen(EntityType<?> type, World world) {
		super(type, world);
	}
	
	public EntityFlatScreen(World world, double x, double y, double z) {
		this(EntityList.FLATSCREEN, world);
		this.updatePosition(x, y, z);
	}
	
	public EntityFlatScreen(World world, double x, double y, double z, Vec3d lookAt) {
		this(EntityList.FLATSCREEN, world);
		this.updatePosition(x, y, z);
		this.getDataTracker().set(LOOK_AT_POS_X, (float)lookAt.x);
		this.getDataTracker().set(LOOK_AT_POS_Y, (float)lookAt.y);
		this.getDataTracker().set(LOOK_AT_POS_Z, (float)lookAt.z);
	}
	
	public Vec3d getLookAtPos() {
		return new Vec3d(this.getDataTracker().get(LOOK_AT_POS_X), this.getDataTracker().get(LOOK_AT_POS_Y), this.getDataTracker().get(LOOK_AT_POS_Z));
	}

	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(LOOK_AT_POS_X, 0f);
		this.getDataTracker().startTracking(LOOK_AT_POS_Y, 0f);
		this.getDataTracker().startTracking(LOOK_AT_POS_Z, 0f);
	}
	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
		this.getDataTracker().set(LOOK_AT_POS_X, tag.getFloat("LookAtX"));
		this.getDataTracker().set(LOOK_AT_POS_Y, tag.getFloat("LookAtY"));
		this.getDataTracker().set(LOOK_AT_POS_Z, tag.getFloat("LookAtZ"));
	}
	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
		tag.putFloat("LookAtX", this.getDataTracker().get(LOOK_AT_POS_X));
		tag.putFloat("LookAtY", this.getDataTracker().get(LOOK_AT_POS_Y));
		tag.putFloat("LookAtZ", this.getDataTracker().get(LOOK_AT_POS_Z));
	}
	
	@Override
	public boolean interact(PlayerEntity player, Hand hand) {
		if(!player.world.isClient) {
			if(player.isSneaking()) {
				this.kill();
				player.world.spawnEntity(new ItemEntity(player.world,
						this.getPosVector().x, this.getPosVector().y, this.getPosVector().z,
						new ItemStack(ItemList.ITEM_FLATSCREEN)));
			}
		}else {
			if(!player.isSneaking()) {
				MinecraftClient.getInstance().openScreen(new GuiFocus());
			}
		}
		return true;
	}
	
	@Override
	public boolean collides() {
		return true;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}

}