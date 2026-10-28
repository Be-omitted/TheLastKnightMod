package cards.Attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import power.ConsensusSpineLancePower;
import helper.TheLastKnightModHelper;
import pathes.AbstractCardEnum;

public class Charge extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID+"Charge";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int DAZE_AMOUNT = 2;

    public Charge(){
        super(ID, cardStrings.NAME, "img/card/Charge.png", 2, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.TheLastKnight_COLOR, CardRarity.BASIC,
                CardTarget.ENEMY);
        this.tags.add(CardTags.STRIKE);
        this.baseDamage = 10;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
        this.updateRawDescription();
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ConsensusSpineLancePower(p, 1, Integer.parseInt(cardStrings.EXTENDED_DESCRIPTION[0]), DAZE_AMOUNT), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new Charge();
    }
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeDamage(3);
            this.updateRawDescription();
        }
    }

    private void updateRawDescription() {
        String baseDescription = this.upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
        this.rawDescription = baseDescription + this.baseDamage
                + cardStrings.EXTENDED_DESCRIPTION[1];
        this.initializeDescription();

    }

}
