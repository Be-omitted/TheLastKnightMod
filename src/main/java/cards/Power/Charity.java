package cards.Power;

import cards.Virtue;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
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
 * 仁爱
 * 获得意志，并在弃牌堆中加入两张本牌。
 */
public class Charity extends Virtue {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Charity";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Charity.png";
    // 费用
    private static final int COST = 1;
    // 获得的意志层数
    private static final int BASE_WILL = 2;
    // 升级增加的意志层数
    private static final int UPGRADE_WILL = 2;

    public Charity() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_WILL;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new WillPower(p, this.magicNumber), this.magicNumber));
        // 复制一张本牌并手动升级，升级后会生成仁爱+
        AbstractCard copy = this.makeCopy();
        if (this.upgraded) {
            copy.upgrade();
        }
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInDiscardAction(copy, 2));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Charity();
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
