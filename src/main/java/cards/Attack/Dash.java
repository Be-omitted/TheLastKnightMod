package cards.Attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import pathes.AbstractCardEnum;

public class Dash extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Dash";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int COST = 1;
    private static final int BASE_DAMAGE = 9;
    private static final int UPGRADE_DAMAGE = 5;
    private static final int DISCARD_AMOUNT = 2;
    private static final int BURN_AMOUNT = 1;

    public Dash() {
        super(ID, cardStrings.NAME, "img/card/Dash.png", COST, cardStrings.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        // 丢弃 2 张牌
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, DISCARD_AMOUNT, !this.upgraded));
        // 将 1 张 灼伤 放入弃牌堆
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Burn(), BURN_AMOUNT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Dash();
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
