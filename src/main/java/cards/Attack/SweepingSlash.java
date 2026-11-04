package cards.Attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cards.Status.Exhaustion;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

/**
 * 横斩
 * 对所有敌人造成伤害并向抽牌堆加入力竭。
 */
public class SweepingSlash extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "SweepingSlash";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    // 费用
    private static final int COST = 1;
    // 基础伤害
    private static final int BASE_DAMAGE = 16;
    // 升级增加的伤害
    private static final int UPGRADE_DAMAGE = 5;
    // 加入抽牌堆的力竭张数
    private static final int EXHAUSTION_AMOUNT = 1;

    public SweepingSlash() {
        super(ID, cardStrings.NAME, "img/card/SweepingSlash.png", COST, cardStrings.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(
                        p,
                        this.multiDamage,
                        this.damageTypeForTurn,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInDrawPileAction(
                        new Exhaustion(),
                        EXHAUSTION_AMOUNT,
                        false,
                        true,
                        false));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SweepingSlash();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
