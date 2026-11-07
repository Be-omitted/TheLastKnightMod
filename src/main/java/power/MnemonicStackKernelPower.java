package power;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;

import static helper.TheLastKnightModHelper.GetPowerNumber;

/**
 * 模肢:忆栈核
 * 每打出一张能力牌，获得层数点能量。
 */
public class MnemonicStackKernelPower extends ModULimbBasePower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID + "MnemonicStackKernelPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);
    // 在 onPlayCard 中登记、等待卡牌动作结束后再结算的次数
    private int pendingUses;

    /**
     * @param amount 每次触发获得的能量
     */
    public MnemonicStackKernelPower(AbstractCreature owner, int amount) {
        super(owner, amount, "MnemonicStackKernelPower");
        this.ID = BASE_ID + GetPowerNumber(BASE_ID);
        this.name = powerStrings.NAME;
        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + Math.max(this.amount, 0) + powerStrings.DESCRIPTIONS[1];
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
        // 登记本张牌，等卡牌动作执行完成后再获得能量
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
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.amount));
    }

    @Override
    public int getMULNumber() {
        return 1;
    }
}
