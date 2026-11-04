package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;
import power.ContingentMultipotentMetaUnitPower;

/**
 * 适应
 * 抽牌并获得模肢:权变多能元。
 */
public class Adaptation extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Adaptation";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    // 费用
    private static final int COST = 1;
    // 抽牌数
    private static final int DRAW_AMOUNT = 2;
    // 触发次数
    private static final int TRIGGER_TIMES = 2;
    // 升级增加的触发次数
    private static final int UPGRADE_TRIGGER_TIMES = 1;

    public Adaptation() {
        super(ID, cardStrings.NAME, "img/card/Adaptation.png", COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = TRIGGER_TIMES;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
        this.updateRawDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 立即抽牌
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, DRAW_AMOUNT));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new ContingentMultipotentMetaUnitPower(p, this.magicNumber, DRAW_AMOUNT, 1), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Adaptation();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
            this.upgradeMagicNumber(UPGRADE_TRIGGER_TIMES);
            this.updateRawDescription();
        }
    }

    private void updateRawDescription() {
        String baseDescription = this.upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
        this.rawDescription = baseDescription
                + this.magicNumber
                + cardStrings.EXTENDED_DESCRIPTION[0]
                + DRAW_AMOUNT
                + cardStrings.EXTENDED_DESCRIPTION[1];
        if (!this.upgraded) this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[2];
        this.initializeDescription();
    }
}
