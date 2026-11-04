package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

public class HelpingHand extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "HelpingHand";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/HelpingHand.png";
    private static final int COST = 1;
    // 回复的生命值
    private static final int BASE_HEAL = 6;
    private static final int UPGRADE_HEAL = 2;
    // 获得的格挡
    private static final int BASE_BLOCK = 8;
    private static final int UPGRADE_BLOCK = 3;

    public HelpingHand() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_HEAL;
        this.magicNumber = this.baseMagicNumber;
        this.baseBlock = BASE_BLOCK;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new HelpingHand();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_HEAL);
            this.upgradeBlock(UPGRADE_BLOCK);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
