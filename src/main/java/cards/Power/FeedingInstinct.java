package cards.Power;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import pathes.AbstractCardEnum;
import power.RostralPortPower;

public class FeedingInstinct extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "FeedingInstinct";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int BASE_HEAL = 2;
    private static final int UPGRADE_HEAL = 2;
    private static final int RVP = 1;

    public FeedingInstinct() {
        super(ID, cardStrings.NAME, "img/card/FeedingInstinct.png", 1, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR,
                CardRarity.BASIC, CardTarget.SELF);
        this.baseMagicNumber = BASE_HEAL;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new RostralPortPower(p, this.magicNumber, RVP), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FeedingInstinct();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_HEAL);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
