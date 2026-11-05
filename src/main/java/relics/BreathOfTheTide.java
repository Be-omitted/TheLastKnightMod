package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helper.TheLastKnightModHelper;
import power.WillPower;

public class BreathOfTheTide extends CustomRelic {
    public static final String ID = TheLastKnightModHelper.HEAD_ID+"BreathOfTheTide";
    private static final String IMG = "img/relics/BreathOfTheTide.png";
    private static final String IMG_OTL = "img/relics/outline/BreathOfTheTide.png";
    // 战斗开始时获得的意志层数
    private static final int WILL_AMOUNT = 6;
    public BreathOfTheTide() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.STARTER, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart(){
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        new WillPower(AbstractDungeon.player, WILL_AMOUNT),
                        WILL_AMOUNT
                )
        );
    }

    @Override
    public void onVictory() {
        // 战斗结束时额外获得一组卡牌奖励
        TheLastKnightModHelper.addExtraCardReward();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BreathOfTheTide();
    }
}
