package mcvmcomputers.gui;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import mcvmcomputers.ClientInitializer;
import mcvmcomputers.MCVmComputersMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class GuiFocus extends Screen{
	private String keyString;
	private ArrayList<Integer> keys;
	
	public GuiFocus() {
		super(new TranslatableText("Focus"));
	}
	
	@Override
	protected void init() {
		MCVmComputersMod.releaseKeys = false;
		
		keyString = "";
		
		int[] unfocusKeys = new int[] {ClientInitializer.glfwUnfocusKey1,
									   ClientInitializer.glfwUnfocusKey2,
									   ClientInitializer.glfwUnfocusKey3,
									   ClientInitializer.glfwUnfocusKey4};

		keys = new ArrayList<>();
		for(int key : unfocusKeys) {
			if(key > 0) {
				keys.add(key);
			}
		}
		
		boolean plus = false;
		for(int key : keys) {
			if(plus) {
				keyString += " + ";
			}
			keyString += ClientInitializer.getKeyName(key);
			plus = true;
		}
	}
	
	@Override
	public void render(int wmouseX, int wmouseY, float delta) {
		long window = minecraft.getWindow().getHandle();
		DoubleBuffer mX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer mY = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(window, mX, mY);
		double mouseX = mX.get();
		double mouseY = mY.get();
		mX.clear();
		mY.clear();
		MCVmComputersMod.mouseCurX = mouseX;
		MCVmComputersMod.mouseCurY = mouseY;
		MCVmComputersMod.leftMouseButton = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
		MCVmComputersMod.middleMouseButton = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_MIDDLE) == GLFW.GLFW_PRESS;
		MCVmComputersMod.rightMouseButton = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
		
		this.font.draw("Press " + keyString + " to lose focus", 4, 4, -1);
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		
		boolean pressed = true;
		for(int key : keys) {
			if(GLFW.glfwGetKey(window, key) == GLFW.GLFW_RELEASE) {
				pressed = false;
				break;
			}
		}
		
		if(pressed) {
			minecraft.openScreen(null);
		}
		
		super.render(wmouseX, wmouseY, delta);
	}
	
	@Override
	public void removed() {
		MCVmComputersMod.releaseKeys = true;
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
