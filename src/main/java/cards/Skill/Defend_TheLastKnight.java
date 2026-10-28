package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import pathes.AbstractCardEnum;

public class Defend_TheLastKnight extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID+"Defend_TheLastKnight";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public  Defend_TheLastKnight(){
        super(ID, cardStrings.NAME, "img/card/Defend_TheLastKnight.png", 1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.TheLastKnight_COLOR, CardRarity.BASIC,
                CardTarget.SELF);
        this.tags.add(CardTags.STARTER_DEFEND);
        this.baseBlock = 5;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Defend_TheLastKnight();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeDamage(3);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

}
