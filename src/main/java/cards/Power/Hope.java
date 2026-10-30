package cards.Power;

import cards.Virtue;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import pathes.AbstractCardEnum;
import power.WillPower;

/**
 * 希望
 * 获得意志并使手牌本回合费用降低。
 */
public class Hope extends Virtue {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Hope";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Hope.png";
    // 费用
    private static final int COST = 2;
    // 获得的意志层数
    private static final int BASE_WILL = 10;

    public Hope() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = BASE_WILL;
        this.magicNumber = this.baseMagicNumber;
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new WillPower(p, this.magicNumber), this.magicNumber));
        // 当前手牌费用 -1（最低 0），打出前生效
        for (AbstractCard card : p.hand.group) {
            if (card != this) {
                card.setCostForTurn(card.costForTurn - 1);
                card.upgrade();
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Hope();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
