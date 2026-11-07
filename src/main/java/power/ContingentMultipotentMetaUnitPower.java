package power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.PowerHistory;
import helper.TheLastKnightModHelper;

import static helper.TheLastKnightModHelper.GetPowerNumber;

/**
 * 打出牌时抽牌，耗尽后获得模肢:冗痕
 */
public class ContingentMultipotentMetaUnitPower extends ModULimbBasePower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID + "ContingentMultipotentMetaUnitPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);
    // History 为空时使用的默认抽牌数与冗痕数
    private static final int DEFAULT_DRAW = 1;
    private static final int DEFAULT_VESTIGE = 1;
    private final int drawAmount;  // 单次牌数
    private final int VAmount;
    // 在 onPlayCard 中登记、等待卡牌动作结束后再结算的次数
    private int pendingUses;

    /**
     * @param amount 使用次数
     * @param drawAmount 单次抽牌数
     * @param v_amount 结束后额外冗痕数
     */
    public ContingentMultipotentMetaUnitPower(AbstractCreature owner, int amount, int drawAmount, int v_amount) {
        super(owner, amount, "ContingentMultipotentMetaUnitPower");
        this.ID = BASE_ID + GetPowerNumber(BASE_ID);
        this.name = powerStrings.NAME;
        this.drawAmount = drawAmount;
        this.VAmount = v_amount;
        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    /**
     * undomod 等按 (owner, amount) 反射重建时使用，额外参数从 History 取。
     */
    public ContingentMultipotentMetaUnitPower(AbstractCreature owner, int amount) {
        this(owner, amount, popHistory());
    }

    private ContingentMultipotentMetaUnitPower(AbstractCreature owner, int amount, int[] history) {
        this(owner, amount, history[0], history[1]);
    }

    private static int[] popHistory() {
        Object[] data = PowerHistory.pop(ContingentMultipotentMetaUnitPower.class);
        if (data == null) {
            return new int[]{DEFAULT_DRAW, DEFAULT_VESTIGE};
        }
        return new int[]{(Integer) data[0], (Integer) data[1]};
    }

    @Override
    public void onRemove() {
        PowerHistory.push(ContingentMultipotentMetaUnitPower.class, this.drawAmount, this.VAmount);
        super.onRemove();
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
        // 登记本张牌，等卡牌动作执行完成后再抽牌
        this.pendingUses++;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        // 只结算本张牌在 onPlayCard 中登记过的触发
        if (this.pendingUses <= 0) {
            return;
        }
        this.pendingUses--;
        if (this.amount <= 0) {
            return;
        }
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.owner, this.drawAmount));

        this.amount--;
        this.updateDescription();
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            // 生成自己的
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
                    new RedundantVestigePower(this.owner, 1+this.VAmount), 1+this.VAmount));
        }
    }

    @Override
    public int getMULNumber() {
        // 不考虑触发时生成的
        return 1;
    }
}
