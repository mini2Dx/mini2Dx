package org.mini2Dx.libgdx.graphics;

import com.badlogic.gdx.graphics.Color;

public class LibgdxReadOnlyColor extends LibgdxColor {
    private static final String EXCEPTION_MESSAGE = "This color is readonly, if you wish to change the values call copy()";

    public LibgdxReadOnlyColor(int rgba8888) {
        super(rgba8888);
    }

    public LibgdxReadOnlyColor(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public LibgdxReadOnlyColor(float r, float g, float b, float a) {
        super(r, g, b, a);
    }

    public LibgdxReadOnlyColor(byte r, byte g, byte b, byte a) {
        super(r, g, b, a);
    }

    public LibgdxReadOnlyColor(Color color) {
        super(color);
    }

    @Override
    public org.mini2Dx.core.graphics.Color set(org.mini2Dx.core.graphics.Color color) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color set(byte r, byte g, byte b, byte a) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color set(float r, float g, float b, float a) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setR(byte r) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setR(float r) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setG(byte g) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setG(float g) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setB(byte b) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setB(float b) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setA(byte a) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setA(float a) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color add(org.mini2Dx.core.graphics.Color color) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color add(byte r, byte g, byte b, byte a) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color add(float r, float g, float b, float a) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color multiply(org.mini2Dx.core.graphics.Color color) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color multiply(float multiplier) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color multiply(byte r, byte g, byte b, byte a) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color multiply(float r, float g, float b, float a) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color subtract(org.mini2Dx.core.graphics.Color color) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color subtract(byte r, byte g, byte b, byte a) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color subtract(float r, float g, float b, float a) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color lerp(org.mini2Dx.core.graphics.Color color, float t) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color lerp(byte r, byte g, byte b, byte a, float t) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public org.mini2Dx.core.graphics.Color lerp(float r, float g, float b, float a, float t) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }
}
