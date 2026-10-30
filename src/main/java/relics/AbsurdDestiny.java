package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helper.TheLastKnightModHelper;

public class AbsurdDestiny extends CustomRelic {
    public static final String ID = TheLastKnightModHelper.HEAD_ID + "AbsurdDestiny";
    private static final String IMG = "img/relics/AbsurdDestiny.png";
    private static final String IMG_OTL = "img/relics/outline/AbsurdDestiny.png";
    // 拾起时获得的金额
    private static final int GOLD_AMOUNT = 999;

    public AbsurdDestiny() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void onEquip() {
        if (AbstractDungeon.player == null) {
            return;
        }
        AbstractDungeon.player.gainGold(GOLD_AMOUNT);
        this.flash();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AbsurdDestiny();
    }
}
