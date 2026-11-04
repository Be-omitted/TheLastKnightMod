package cards.Attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cards.Status.Exhaustion;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

public class PerishWithTheEnemy extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "PerishWithTheEnemy";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/PerishWithTheEnemy.png";
    private static final int COST = 3;
    private static final int BASE_DAMAGE = 40;
    private static final int UPGRADE_DAMAGE = 10;
    // 加入弃牌堆的力竭数量
    private static final int EXHAUSTION_AMOUNT = 2;

    public PerishWithTheEnemy() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HEAVY));
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInDiscardAction(new Exhaustion(), EXHAUSTION_AMOUNT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PerishWithTheEnemy();
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
