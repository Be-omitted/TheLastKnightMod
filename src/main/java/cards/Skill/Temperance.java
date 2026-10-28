package cards.Skill;

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

public class Temperance extends Virtue {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Temperance";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Temperance.png";
    private static final int COST = 0;
    // 获得的意志层数
    private static final int BASE_WILL = 4;
    private static final int UPGRADE_WILL = 2;

    public Temperance() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_WILL;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard card) {
        super.triggerOnOtherCardPlayed(card);
        // 本回合每打出另一张牌，费用 +1
        if (this.costForTurn >= 0) {
            this.setCostForTurn(this.costForTurn + 1);
        }
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        // “本回合”的加费在下一回合重置回初始费用
        this.resetAttributes();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new WillPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Temperance();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_WILL);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
