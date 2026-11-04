package power;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helper.TheLastKnightModHelper;

/**
 * 模肢:谐枢
 * 获得 模肢:冗痕 时，获得层数点格挡。
 */
public class HarmonicHubPower extends ModULimbBasePower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID + "HarmonicHubPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);

    public HarmonicHubPower(AbstractCreature owner, int amount) {
        super(owner, amount, "HarmonicHubPower");
        this.ID = BASE_ID;
        this.name = powerStrings.NAME;
        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + Math.max(this.amount, 0)
                + powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        super.onApplyPower(power, target, source);
        if (this.owner == null || target != this.owner) {
            return;
        }
        if (!(power instanceof RedundantVestigePower)) {
            return;
        }
        if (power.amount <= 0 || this.amount <= 0) {
            return;
        }
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(this.owner, this.owner, this.amount));
    }

    @Override
    public int getMULNumber() {
        return this.amount;
    }
}
