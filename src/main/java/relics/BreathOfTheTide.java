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
    private static final String IMG = "img/relics/Bloom.png";
    private static final String IMG_OTL = "img/relics/outline/Bloom.png";
    public BreathOfTheTide() {
        super(ID, ImageMaster.loadImage(IMG), RelicTier.STARTER, AbstractRelic.LandingSound.CLINK);

    }

    @Override
    public void atBattleStart(){
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        new WillPower(AbstractDungeon.player, 6),
                        6
                )
        );
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
