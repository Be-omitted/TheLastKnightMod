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
import power.ChloroplastPower;

public class Photosynthesis extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Photosynthesis";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Photosynthesis.png";
    private static final int COST = 1;
    // 每打出多少张牌触发一次
    private static final int CARDS_PER_TRIGGER = 10;
    // 每次触发的能量数
    private static final int ENERGY_GAIN = 1;

    public Photosynthesis() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p,
                        new ChloroplastPower(p, CARDS_PER_TRIGGER, CARDS_PER_TRIGGER, ENERGY_GAIN),
                        CARDS_PER_TRIGGER));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Photosynthesis();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 固有
            this.isInnate = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
