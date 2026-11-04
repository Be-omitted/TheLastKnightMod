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
import patches.AbstractCardEnum;
import power.WillPower;

/**
 * 慷慨
 * 消耗金币获得意志。
 */
public class Generosity extends Virtue {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Generosity";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Generosity.png";
    // 费用
    private static final int COST = 0;
    // 获得的意志层数
    private static final int BASE_WILL = 3;
    // 升级增加的意志层数
    private static final int UPGRADE_WILL = 2;
    // 打出需要消耗的金币
    private static final int BASE_GOLD_COST = 20;
    // 升级后打出需要消耗的金币
    private static final int UPGRADE_GOLD_COST = 15;

    public Generosity() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_WILL;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) {
            return false;
        }
        // 金币不足时不能打出
        return p.gold >= getGoldCost();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.loseGold(getGoldCost());
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new WillPower(p, this.magicNumber), this.magicNumber));
    }

    // 升级后金币消耗降低，描述中的 20/15 与此对应
    private int getGoldCost() {
        return this.upgraded ? UPGRADE_GOLD_COST : BASE_GOLD_COST;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Generosity();
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
