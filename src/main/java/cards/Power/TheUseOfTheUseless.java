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
import patches.AbstractCardEnum;
import power.HarmonicHubPower;

public class TheUseOfTheUseless extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "TheUseOfTheUseless";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/TheUseOfTheUseless.png";
    private static final int COST = 1;
    // 每次获得模肢:冗痕时获得的格挡
    private static final int BASE_BLOCK = 2;
    private static final int UPGRADE_BLOCK = 1;

    public TheUseOfTheUseless() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = BASE_BLOCK;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p,
                        new HarmonicHubPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new TheUseOfTheUseless();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_BLOCK);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
