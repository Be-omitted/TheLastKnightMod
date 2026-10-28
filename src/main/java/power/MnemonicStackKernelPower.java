package power;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;

import static helper.TheLastKnightModHelper.GetPowerNumber;

public class MnemonicStackKernelPower extends ModULimbBasePower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID + "MnemonicStackKernelPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);

    public MnemonicStackKernelPower(AbstractCreature owner, int amount) {
        super(owner, amount, "MnemonicStackKernelPower");
        this.ID = BASE_ID + GetPowerNumber(BASE_ID);
        this.name = powerStrings.NAME;
        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + Math.max(this.amount, 0);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card == null) {
            return;
        }
        if (card.type != AbstractCard.CardType.POWER) {
            return;
        }
        if (this.amount <= 0) {
            return;
        }
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.amount));
    }

    @Override
    public int getMULNumber() {
        return 1;
    }
}
