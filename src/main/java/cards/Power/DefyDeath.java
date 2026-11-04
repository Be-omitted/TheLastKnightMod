package cards.Power;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

public class DefyDeath extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "DefyDeath";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/DefyDeath.png";
    private static final int COST = 3;
    // 基础/升级后调整到的最大生命百分比
    private static final int BASE_PERCENT = 30;
    private static final int UPGRADE_PERCENT = 50;

    public DefyDeath() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int percent = this.upgraded ? UPGRADE_PERCENT : BASE_PERCENT;
        int targetHealth = Math.max(1, p.maxHealth * percent / 100);
        p.currentHealth = targetHealth;
        p.healthBarUpdatedEvent();
    }

    @Override
    public AbstractCard makeCopy() {
        return new DefyDeath();
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
