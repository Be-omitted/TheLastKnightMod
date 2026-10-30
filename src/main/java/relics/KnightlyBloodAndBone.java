package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helper.TheLastKnightModHelper;
import power.WillPower;

public class KnightlyBloodAndBone extends CustomRelic {
    public static final String ID = TheLastKnightModHelper.HEAD_ID + "KnightlyBloodAndBone";
    private static final String IMG = "img/relics/KnightlyBloodAndBone.png";
    private static final String IMG_OTL = "img/relics/outline/KnightlyBloodAndBone.png";
    // 每回合开始时获得的意志层数
    private static final int WILL_AMOUNT = 6;

    public KnightlyBloodAndBone() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void onEquip() {
        // 由初始遗物升级而来，替换掉海潮的气息
        if (AbstractDungeon.player != null) {
            AbstractDungeon.player.loseRelic(BreathOfTheTide.ID);
        }
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.player == null) {
            return;
        }
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new WillPower(AbstractDungeon.player, WILL_AMOUNT), WILL_AMOUNT));
    }

    @Override
    public boolean canSpawn() {
        // 只有持有初始遗物时才会作为首领遗物出现
        return AbstractDungeon.player != null
                && AbstractDungeon.player.hasRelic(BreathOfTheTide.ID);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new KnightlyBloodAndBone();
    }
}
