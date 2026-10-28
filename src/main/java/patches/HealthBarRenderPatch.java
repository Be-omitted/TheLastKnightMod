package patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import character.TheLastKnight;
import helper.TheLastKnightModHelper;

import java.lang.reflect.Field;

@SpirePatch(clz = AbstractCreature.class, method = "renderHealthText", paramtypez = { SpriteBatch.class, float.class })
public class HealthBarRenderPatch {

    // 深蓝(0x000081FF)，与角色主题色 TheLastKnightModCore.LIGHT 保持一致
    private static final Color NEGATIVE_WILL_COLOR = new Color(0x000081FF);
    // 血条高度与血条相对 y 偏移，取自 AbstractCreature 的私有常量
    // （通过反射读取并缓存；读取失败时退回原版 12-18-2022 的取值，避免写死导致不精确）
    private static final float HEALTH_BAR_HEIGHT = readStaticFloat("HEALTH_BAR_HEIGHT", 20.0F);
    private static final float HEALTH_BAR_OFFSET_Y = readStaticFloat("HEALTH_BAR_OFFSET_Y", -28.0F);

    // Prefix：在原版绘制血量文本之前执行
    public static void Prefix(AbstractCreature self, SpriteBatch sb, float y) {
        // 只处理本模组的角色，其它生物保持原样
        if (!(self instanceof TheLastKnight)) {
            return;
        }
        // 隐藏战斗元素或血条不可见时不绘制
        if (Settings.hideCombatElements || self.hbAlpha <= 0.0F || self.currentHealth <= 0) {
            return;
        }
        // 计算净意志：意志(WillPower)层数 减去 模肢:冗痕(RedundantVestigePower)层数
        int willH = TheLastKnightModHelper.GetWillH((TheLastKnight) self);
        // 仅当净意志为负数（即战斗结束时将会失去生命）时才绘制深蓝提示
        if (willH >= 0) {
            return;
        }
        // 将要失去的生命值占最大生命的比例，并限制不超过整条血条
        float ratio = Math.min(1.0F, (-willH) / (float) self.maxHealth);
        // 血条左端 x：与原版 AbstractCreature.renderHealth 保持一致（hb 中心左移半个宽度）
        float barX = self.hb.cX - self.hb.width * 0.5F;
        // 血条底部 y：渲染传入的 y 已经包含血条弹出动画的偏移，这里再加上血条自身的偏移
        float barY = y + HEALTH_BAR_OFFSET_Y;
        // 深蓝区段长度 = 血条总宽度 × 比例
        float blueWidth = self.hb.width * ratio;

        // 深蓝区段跟随血条整体的淡入淡出透明度
        NEGATIVE_WILL_COLOR.a = self.hbAlpha;
        sb.setColor(NEGATIVE_WILL_COLOR);
        // 左侧圆角端点
        sb.draw(ImageMaster.HEALTH_BAR_L, barX - HEALTH_BAR_HEIGHT, barY, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        // 深蓝主体：占据血条左侧 blueWidth 的长度
        sb.draw(ImageMaster.HEALTH_BAR_B, barX, barY, blueWidth, HEALTH_BAR_HEIGHT);
        // 恢复画笔颜色，避免影响之后血量文本的渲染
        sb.setColor(Color.WHITE);
    }

    // 反射读取 AbstractCreature 的私有静态 float 常量
    private static float readStaticFloat(String fieldName, float fallback) {
        try {
            Field field = AbstractCreature.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getFloat(null);
        } catch (Exception e) {
            return fallback;
        }
    }
}
