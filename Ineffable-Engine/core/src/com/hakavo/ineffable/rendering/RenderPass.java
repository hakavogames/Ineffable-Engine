package com.hakavo.ineffable.rendering;

import com.hakavo.ineffable.core.Transform;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.*;

public abstract class RenderPass {
    protected FrameBuffer frameBuffer;
    public abstract void init(Engine engine);
    public abstract void begin();
    public abstract void render(Renderable renderable);
    public abstract void end();
    public final FrameBuffer getFrameBuffer() {
        return frameBuffer;
    }
    public void bind(ShaderProgram shader) {
    }
}
