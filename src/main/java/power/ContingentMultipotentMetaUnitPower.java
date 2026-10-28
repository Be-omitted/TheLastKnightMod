package power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;

import static helper.TheLastKnightModHelper.GetPowerNumber;

public class ContingentMultipotentMetaUnitPower extends ModULimbBasePower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID + "ContingentMultipotentMetaUnitPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);
    private final int drawAmount;  // 单次牌数
    private final int VAmount;

    public ContingentMultipotentMetaUnitPower(AbstractCreature owner, int amount, int drawAmount, int v_amount) {
        super(owner, amount, "ContingentMultipotentMetaUnitPower");
        this.ID = BASE_ID + GetPowerNumber(BASE_ID);
        this.name = powerStrings.NAME;
        this.drawAmount = drawAmount;
        this.VAmount = v_amount;
        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + Math.max(this.amount, 0)
                + powerStrings.DESCRIPTIONS[1] + this.drawAmount
                + powerStrings.DESCRIPTIONS[2] + this.VAmount + powerStrings.DESCRIPTIONS[3];
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card == null) {
            return;
        }
        if (this.amount <= 0) {
            return;
        }
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.owner, this.drawAmount));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
                new RedundantVestigePower(this.owner, this.VAmount), this.VAmount));
        this.amount--;
        this.updateDescription();
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            // 生成自己的
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
                    new RedundantVestigePower(this.owner, 1), 1));
        }
    }

    @Override
    public int getMULNumber() {
        // 不考虑触发时生成的
        return 1;
    }
}
