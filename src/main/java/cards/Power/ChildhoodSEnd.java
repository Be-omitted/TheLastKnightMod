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
import pathes.AbstractCardEnum;
import power.LineagePolicyKernelPower;

/**
 * 童年的终结
 * 获得模肢:谱策核，使随机模肢允许更稀有的结果。
 */
public class ChildhoodSEnd extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "ChildhoodSEnd";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/ChildhoodSEnd.png";
    // 费用
    private static final int COST = 2;

    public ChildhoodSEnd() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得 模肢:谱策核，使随机模肢允许更稀有的结果
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new LineagePolicyKernelPower(p), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ChildhoodSEnd();
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
