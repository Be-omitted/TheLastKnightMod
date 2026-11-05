package power;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helper.TheLastKnightModHelper;

/**
 * 狂徒妄念
 * 免死标记：模肢:冗痕的伤害最多将生命扣至 1。
 */
public class FanaticSDelusionPower extends AbstractPower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID + "FanaticSDelusionPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);

    /**
     * 创建免死标记，层数固定为 -1 以隐藏层数显示。
     */
    public FanaticSDelusionPower(AbstractCreature owner) {
        this.ID = BASE_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        // 该 Power 只作为标记使用，不显示层数
        this.amount = -1;
        this.type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("img/power/FanaticSDelusionPower84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("img/power/FanaticSDelusionPower32.png"), 0, 0, 32, 32);
        this.updateDescription();
    }

    /**
     * undomod 等按 (owner, amount) 反射重建时使用。
     * 该 Power 只作为免死标记，层数固定为 -1，忽略传入的 amount。
     */
    public FanaticSDelusionPower(AbstractCreature owner, int amount) {
        this(owner);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        // 描述文本依赖 GetWillHText，获得本 Power 后同步刷新可能存在的意志与冗痕描述
        if (this.owner == null) {
            return;
        }
        String willText = TheLastKnightModHelper.GetWillHText(this.owner);
        if (this.owner.hasPower(WillPower.BASE_ID)) {
            ((WillPower) this.owner.getPower(WillPower.BASE_ID)).syncWR(willText);
        }
        if (this.owner.hasPower(RedundantVestigePower.BASE_ID)) {
            ((RedundantVestigePower) this.owner.getPower(RedundantVestigePower.BASE_ID)).syncWR(willText);
        }
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }
    // 免死判定在 TheLastKnightModHelper.GetWillH 中执行
}
