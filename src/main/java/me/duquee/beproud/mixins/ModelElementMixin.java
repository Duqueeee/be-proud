package me.duquee.beproud.mixins;

import com.google.gson.JsonObject;
import net.minecraft.client.render.model.json.ModelElement;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelElement.Deserializer.class)
public class ModelElementMixin {

    @Shadow
    private Vector3f deserializeVec3f(JsonObject object, String name) {
        throw new AssertionError();
    }

    @Inject(method = "deserializeFrom(Lcom/google/gson/JsonObject;)Lorg/joml/Vector3f;", at = @At("HEAD"), cancellable = true)
    private void increaseSizeLimit(JsonObject object, CallbackInfoReturnable<Vector3f> cir) {
        Vector3f vector3f = this.deserializeVec3f(object, "from");
        if (!(vector3f.y() < -32.0F) && !(vector3f.x() < -48.0F) && !(vector3f.z() < -16.0F) && !(vector3f.x() > 32.0F) && !(vector3f.y() > 32.0F) && !(vector3f.z() > 32.0F)) {
            cir.setReturnValue(vector3f);
        }
    }

}
