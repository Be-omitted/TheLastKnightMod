package power;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;

import static helper.TheLastKnightModHelper.GetPowerNumber;

/**
 * 模肢:喙口
 * 击杀敌人时回复生命并获得模肢:冗痕。
 */
public class RostralPortPower extends ModULimbBasePower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID + "RostralPortPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);
    public int RVP;

    /**
     * @param amount 每次击杀回复的生命
     * @param RVP_amount 每次击杀获得的模肢:冗痕层数
     */
    public RostralPortPower(AbstractCreature owner, int amount, int RVP_amount) {
        super(owner, amount, "RostralPortPower");
        this.RVP = RVP_amount;
        this.ID = BASE_ID + GetPowerNumber(BASE_ID);
        this.name = powerStrings.NAME;
        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        int healAmount = Math.max(this.amount, 0);
        this.description = powerStrings.DESCRIPTIONS[0] + healAmount + powerStrings.DESCRIPTIONS[1] + this.RVP + powerStrings.DESCRIPTIONS[2];
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (this.owner == null || target == null || target == this.owner) {
            return;
        }
        if (damageAmount <= 0 || this.amount <= 0) {
            return;
        }
        if (!(target instanceof AbstractMonster)) {
            return;
        }
        AbstractMonster monster = (AbstractMonster) target;
        if (monster.halfDead || monster.hasPower("Minion")) {
            return;
        }
        // onAttack 在扣血前触发，因此把击杀判定延后到当前伤害动作结算之后
        AbstractDungeon.actionManager.addToBottom(
                new KillTriggerAction(this, monster, this.amount));
    }
    @Override
    public int getMULNumber(){
        return 1;
    }
    /**
     * 在当前伤害结算完成后检查目标是否死亡，并触发喙口的回复与冗痕效果。
     */
    private static class KillTriggerAction extends AbstractGameAction {
        private final RostralPortPower power;
        private final AbstractMonster target;
        private final int healAmount;

        /**
         * @param power 触发效果的喙口 Power
         * @param target 本次伤害的目标
         * @param healAmount 击杀后回复的生命
         */
        private KillTriggerAction(RostralPortPower power, AbstractMonster target, int healAmount) {
            this.power = power;
            this.target = target;
            this.healAmount = healAmount;
            this.actionType = ActionType.DAMAGE;
        }

        @Override
        public void update() {
            if (target.isDying || target.currentHealth <= 0) {
                if (!target.halfDead && !target.hasPower("Minion")) {
                    this.power.flash();
                    AbstractDungeon.actionManager.addToBottom(
                            new HealAction(this.power.owner, this.power.owner, this.healAmount));
                    AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(this.power.owner, this.power.owner,
                                    new RedundantVestigePower(this.power.owner, this.power.RVP),
                                    this.power.RVP));
                }
            }
            this.isDone = true;
        }

    }
}
