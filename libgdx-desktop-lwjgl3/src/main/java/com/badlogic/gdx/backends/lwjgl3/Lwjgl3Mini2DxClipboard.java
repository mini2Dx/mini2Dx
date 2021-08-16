package com.badlogic.gdx.backends.lwjgl3;

import com.badlogic.gdx.Gdx;
import org.lwjgl.glfw.GLFW;

public class Lwjgl3Mini2DxClipboard extends Lwjgl3Clipboard {

    @Override
    public String getContents () {
        return GLFW.glfwGetClipboardString(((Lwjgl3Mini2DxGraphics) Gdx.graphics).getWindow().getWindowHandle());
    }

    @Override
    public void setContents (String content) {
        GLFW.glfwSetClipboardString(((Lwjgl3Mini2DxGraphics)Gdx.graphics).getWindow().getWindowHandle(), content);
    }
}
