/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.style;

import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.serialization.annotation.Field;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;

/**
 *
 */
public class CheckboxStyleRule extends ParentStyleRule {
	@Field
	private String enabledBox;
	@Field
	private String disabledBox;
	@Field
	private String disabledCheck;
	@Field(optional = true)
	private String disabledUncheck;
	@Field(optional = true)
	private String hoveredCheck;
	@Field(optional = true)
	private String hoveredUncheck;
	@Field
	private String enabledCheck;
	@Field(optional = true)
	private String enabledUncheck;

	private NinePatch enabledNinePatch, disabledNinePatch;
	private TextureRegion disabledCheckTextureRegion, disabledUncheckTextureRegion, enabledCheckTextureRegion,
			enabledUncheckTextureRegion, hoveredCheckTextureRegion, hoveredUncheckTextureRegion;
	
	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}
		super.prepareAssets(theme, fileHandleResolver, assetManager);
		
		enabledNinePatch = new NinePatch(new TextureRegion(theme.getTextureAtlas().findRegion(enabledBox)), getNinePatchLeft(),
				getNinePatchRight(), getNinePatchTop(), getNinePatchBottom());
		disabledNinePatch = new NinePatch(new TextureRegion(theme.getTextureAtlas().findRegion(disabledBox)), getNinePatchLeft(),
				getNinePatchRight(), getNinePatchTop(), getNinePatchBottom());
		enabledCheckTextureRegion = new TextureRegion(theme.getTextureAtlas().findRegion(enabledCheck));
		disabledCheckTextureRegion = new TextureRegion(theme.getTextureAtlas().findRegion(disabledCheck));
		
		if(disabledUncheck != null) {
			disabledUncheckTextureRegion = new TextureRegion(theme.getTextureAtlas().findRegion(disabledUncheck));
		}
		if(enabledUncheck != null) {
			enabledUncheckTextureRegion = new TextureRegion(theme.getTextureAtlas().findRegion(enabledUncheck));
		}
		if(hoveredCheck == null) {
			hoveredCheckTextureRegion = enabledCheckTextureRegion;
		} else {
			hoveredCheckTextureRegion = new TextureRegion(theme.getTextureAtlas().findRegion(hoveredCheck));
		}
		if (hoveredUncheck != null) {
			hoveredUncheckTextureRegion = new TextureRegion(theme.getTextureAtlas().findRegion(hoveredUncheck));
		}
	}

	public NinePatch getEnabledNinePatch() {
		return enabledNinePatch;
	}

	public NinePatch getDisabledNinePatch() {
		return disabledNinePatch;
	}

	public TextureRegion getDisabledCheckTextureRegion() {
		return disabledCheckTextureRegion;
	}

	public TextureRegion getDisabledUncheckTextureRegion() {
		return disabledUncheckTextureRegion;
	}

	public TextureRegion getEnabledCheckTextureRegion() {
		return enabledCheckTextureRegion;
	}

	public TextureRegion getEnabledUncheckTextureRegion() {
		return enabledUncheckTextureRegion;
	}

	public TextureRegion getHoveredCheckTextureRegion() {
		return hoveredCheckTextureRegion;
	}

	public TextureRegion getHoveredUncheckTextureRegion() {
		return hoveredUncheckTextureRegion;
	}
}
