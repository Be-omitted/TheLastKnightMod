package cards.Skill;

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
import power.LysosomalInjectionGlandPower;

public class CorrosiveLiquid extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "CorrosiveLiquid";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int COST = 1;
    private static final int TRIGGER_TIMES = 3;

    // 中毒层数
    private int Poison;
    private static final int Poison_up = 2;

    public CorrosiveLiquid() {
        super(ID, cardStrings.NAME, "img/card/CorrosiveLiquid.png", COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = TRIGGER_TIMES;
        this.magicNumber = this.baseMagicNumber;
        this.Poison = 2;
        this.updateRawDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new LysosomalInjectionGlandPower(p, this.magicNumber, this.Poison), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CorrosiveLiquid();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.Poison += Poison_up;
            this.updateRawDescription();
        }
    }

    private void updateRawDescription() {
        String baseDescription = this.upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
        this.rawDescription = baseDescription
                + this.Poison
                + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }
}
