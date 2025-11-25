// Save this file as LayerHeadMarker.java in net/minecraft/client/renderer/entity/layers/
package net.minecraft.client.renderer.entity.layers;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class LayerHeadMarker implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    public LayerHeadMarker(RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float par2, float par3, float partialTicks, float par5, float par6, float par7, float scale) {
        // Get the player who is viewing the world (the camera)
        Entity viewer = this.playerRenderer.getRenderManager().livingPlayer;

        // Don't render the marker for the local player or for players in spectator mode, etc.
        if (viewer == null || entitylivingbaseIn == viewer || !entitylivingbaseIn.isEntityAlive() || entitylivingbaseIn.isPlayerSleeping() || entitylivingbaseIn.isInvisible()) {
            return;
        }

        // --- Core Logic Starts Here ---

        GlStateManager.pushMatrix(); // Save the current state of the OpenGL matrix

        // 1. Disable Depth Testing to render through walls
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false); // Don't write to the depth buffer

        // 2. Position the marker above the player's head
        float yOffset = entitylivingbaseIn.height + 0.5F;
        if (entitylivingbaseIn.isSneaking()) {
            yOffset -= 0.25F;
        }
        GlStateManager.translate(0.0F, yOffset, 0.0F);

        // 3. Make the head always face the camera (billboard effect)
        GlStateManager.rotate(-this.playerRenderer.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.playerRenderer.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        
        // --- DYNAMIC SCALING LOGIC ---
        // Calculate distance from the viewer to the entity
        float distance = entitylivingbaseIn.getDistanceToEntity(viewer);

        // Define how the marker should scale with distance. You can tweak these values!
        final float MIN_SCALE = 0.6F;     // The smallest the marker can be (at close range).
        final float MAX_SCALE = 8.0F;     // The largest the marker can be (to prevent it getting huge).
        final float SCALE_RATE = 0.025F;  // How much the scale increases per block of distance. A good starting point.

        // Calculate the desired scale by multiplying the distance by our rate.
        float desiredScale = distance * SCALE_RATE;

        // Clamp the scale between our defined min and max values.
        // Math.max ensures it's never smaller than MIN_SCALE.
        // Math.min ensures it's never larger than MAX_SCALE.
        float finalScale = Math.min(MAX_SCALE, Math.max(MIN_SCALE, desiredScale));
        
        // 4. Scale the head to the dynamically calculated size
        GlStateManager.scale(-finalScale, -finalScale, finalScale); // Negative X to flip it back correctly

        // 5. Bind the player's skin texture
        this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationSkin());

        // 6. Render just the head and hat layer
        this.playerRenderer.getMainModel().bipedHead.render(0.0625F);
        if (entitylivingbaseIn.isWearing(EnumPlayerModelParts.HAT)) {
            this.playerRenderer.getMainModel().bipedHeadwear.render(0.0625F);
        }
        
        // 7. Re-enable Depth Testing and restore the state
        // This is EXTREMELY important. Forgetting this will break all subsequent rendering.
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();

        GlStateManager.popMatrix(); // Restore the OpenGL matrix to its original state
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}