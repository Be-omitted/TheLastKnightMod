package cards.Skill;

import cards.Virtue;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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
 * 贞洁
 * 获得格挡与意志，每次打出后获得的意志层数递减。
 */
public class Chastity extends Virtue {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Chastity";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Chastity.png";
    // 费用
    private static final int COST = 1;
    // 基础格挡
    private static final int BASE_BLOCK = 10;
    // 获得的意志层数
    private static final int BASE_WILL = 2;
    // 升级增加的意志层数
    private static final int UPGRADE_WILL = 1;

    public Chastity() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = BASE_BLOCK;
        this.baseMagicNumber = BASE_WILL;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new WillPower(p, this.magicNumber), this.magicNumber));
        // 每打出一次，本场战斗中之后获得的意志 -1
        this.baseMagicNumber = this.baseMagicNumber - 1;
        this.magicNumber = this.baseMagicNumber;
        this.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Chastity();
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
