package power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import helper.PowerHistory;
import helper.TheLastKnightModHelper;

import static helper.TheLastKnightModHelper.GetPowerNumber;

/**
 * 模肢:溶注腺
 * 打出攻击牌时为攻击目标附加中毒，次数耗尽后获得模肢:冗痕。
 */
public class LysosomalInjectionGlandPower extends ModULimbBasePower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID + "LysosomalInjectionGlandPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);
    // History 为空时使用的默认中毒层数
    private static final int DEFAULT_POISON = 1;
    // 每次触发附加的中毒层数
    private final int poisonAmount;
    // 在 onPlayCard 中登记、等待卡牌动作结束后再结算的次数
    private int pendingUses;

    /**
     * @param amount 可触发次数
     * @param poisonAmount 每次触发附加的中毒层数
     */
    public LysosomalInjectionGlandPower(AbstractCreature owner, int amount, int poisonAmount) {
        super(owner, amount, "LysosomalInjectionGlandPower");
        this.ID = BASE_ID + GetPowerNumber(BASE_ID);
        this.name = powerStrings.NAME;
        this.poisonAmount = poisonAmount;
        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    /**
     * undomod 等按 (owner, amount) 反射重建时使用，额外参数从 History 取。
     */
    public LysosomalInjectionGlandPower(AbstractCreature owner, int amount) {
        this(owner, amount, popPoisonAmount());
    }

    private static int popPoisonAmount() {
        Object[] data = PowerHistory.pop(LysosomalInjectionGlandPower.class);
        return data == null ? DEFAULT_POISON : (Integer) data[0];
    }

    @Override
    public void onRemove() {
        PowerHistory.push(LysosomalInjectionGlandPower.class, this.poisonAmount);
        super.onRemove();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + Math.max(this.amount, 0)
                + powerStrings.DESCRIPTIONS[1] + this.poisonAmount
                + powerStrings.DESCRIPTIONS[2];
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card == null || card.type != AbstractCard.CardType.ATTACK) {
            return;
        }
        if (this.amount <= 0) {
            return;
        }
        // 登记本次攻击，等卡牌动作执行完成后再附加中毒
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
        AbstractMonster target = (action.target instanceof AbstractMonster)
                ? (AbstractMonster) action.target
                : null;
        // 攻击目标缺省时（例如群体攻击）随机挑选一个敌人
        if (target == null && AbstractDungeon.getMonsters() != null) {
            target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        }
        // 卡牌动作可能已经击杀了原目标，此时不再附加中毒
        if (target == null || target.isDeadOrEscaped() || target.isDying) {
            return;
        }
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, this.owner,
                new PoisonPower(target, this.owner, this.poisonAmount), this.poisonAmount));
        this.amount--;
        this.updateDescription();
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
                    new RedundantVestigePower(this.owner, 1), 1));
        }
    }

    @Override
    public int getMULNumber() {
        return 1;
    }
}
