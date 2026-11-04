package cards.Attack;

import cards.Virtue;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;
import power.WillPower;

/**
 * 勤勉
 * 获得意志并对敌人多次造成伤害，升级后攻击全体敌人。
 */
public class Diligence extends Virtue {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Diligence";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Diligence.png";
    // 费用
    private static final int COST = 1;
    // 单次伤害
    private static final int BASE_DAMAGE = 2;
    // 攻击次数
    private static final int BASE_HITS = 3;
    // 固定获得的意志层数
    private static final int WILL_GAIN = 1;

    public Diligence() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = BASE_HITS;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new WillPower(p, WILL_GAIN), WILL_GAIN));
        // 按次数逐次造成伤害；升级后目标为全体敌人
        for (int i = 0; i < this.magicNumber; i++) {
            if (this.target == CardTarget.ALL_ENEMY) {
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            } else {
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Diligence();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级后由单体攻击改为攻击所有敌人
            this.target = CardTarget.ALL_ENEMY;
            this.isMultiDamage = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
