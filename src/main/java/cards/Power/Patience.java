package cards.Power;

import cards.Virtue;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;
import power.WillPower;

/**
 * 耐心
 * 抽到时降低本场战斗的费用，打出时获得意志。
 */
public class Patience extends Virtue {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Patience";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Patience.png";
    // 费用
    private static final int COST = 10;
    // 获得的意志层数
    private static final int BASE_WILL = 10;
    // 升级增加的意志层数
    private static final int UPGRADE_WILL = 4;
    // 每次抽到此牌时降低的费用
    private static final int DRAW_COST_REDUCTION = 2;

    public Patience() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_WILL;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        // 每次抽到时费用 -2，本场战斗内累计，最低降到 0
        this.modifyCostForCombat(-DRAW_COST_REDUCTION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new WillPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Patience();
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
