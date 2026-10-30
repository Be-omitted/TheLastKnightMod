package power;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;

/**
 * 模肢:共识棘矛
 * 打出攻击牌时，每层对随机敌人造成伤害并在手牌加入晕眩，同时获得模肢:冗痕。
 */
public class ConsensusSpineLancePower extends ModULimbBasePower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID+"ConsensusSpineLancePower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);
    // 触发后变化为 模肢:冗痕 的层数
    public static final int VESTIGE_AMOUNT = 1;

    private final int HAmount;
    private final int DAomout;

    /**
     * @param amount 可触发次数
     * @param lancedamage 每次触发的伤害
     * @param daze_amount 每次触发加入手牌的晕眩张数
     */
    public ConsensusSpineLancePower(AbstractCreature owner, int amount,int lancedamage, int daze_amount){
        super(owner, amount, "ConsensusSpineLancePower");
        this.ID = BASE_ID + lancedamage;
        this.name = powerStrings.NAME;
        this.HAmount = lancedamage;
        this.DAomout = daze_amount;
        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        int lanceDamage = Math.max(this.amount, 0);
        this.description = powerStrings.DESCRIPTIONS[0] + lanceDamage + powerStrings.DESCRIPTIONS[1]
                + powerStrings.DESCRIPTIONS[2] + DAomout + powerStrings.DESCRIPTIONS[3];
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        // 只有打出攻击牌时才会触发
        if (card == null || card.type != AbstractCard.CardType.ATTACK) {
            return;
        }
        if (this.amount <= 0 || AbstractDungeon.getMonsters() == null) {
            return;
        }

        // 允许多触发
        while (this.amount > 0)
        {
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (target == null) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
                        new RedundantVestigePower(this.owner, this.amount), this.amount));
                return;
            }
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target,
                    new DamageInfo(this.owner, this.HAmount, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Dazed(), DAomout));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
                    new RedundantVestigePower(this.owner, VESTIGE_AMOUNT), VESTIGE_AMOUNT));
            this.amount--;
        }
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }
    @Override
    public int getMULNumber(){
        return this.amount;
    }
}
