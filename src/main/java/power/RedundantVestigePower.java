package power;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import helper.TheLastKnightModHelper;

public class RedundantVestigePower extends ModULimbBasePower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID+"RedundantVestigePower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);
    public RedundantVestigePower(AbstractCreature owner, int amount){
        super(owner, amount, "RedundantVestigePower");
        this.ID = BASE_ID;
        this.name = powerStrings.NAME;
        this.type = PowerType.DEBUFF;
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        int Number = Math.max(this.amount, 0);
        String w = helper.TheLastKnightModHelper.GetWillHText(this.owner);
        this.description = powerStrings.DESCRIPTIONS[0] + Number + powerStrings.DESCRIPTIONS[1]
                + w;
        if (this.owner.hasPower(WillPower.BASE_ID)){
            ((WillPower)this.owner.getPower(WillPower.BASE_ID)).syncWR(w);
        }
    }

    public void syncWR(String w){
        int Number = Math.max(this.amount, 0);
        this.description = powerStrings.DESCRIPTIONS[0] + Number + powerStrings.DESCRIPTIONS[1]
                + w;
    }
    @Override
    public int getMULNumber(){
        return this.amount;
    }
    // 掉血实现在core
}
