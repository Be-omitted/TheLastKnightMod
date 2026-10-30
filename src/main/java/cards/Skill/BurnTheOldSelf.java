package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import pathes.AbstractCardEnum;

/**
 * 燃烧旧我
 * 消耗手牌并获得能量。
 */
public class BurnTheOldSelf extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "BurnTheOldSelf";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/BurnTheOldSelf.png";
    // 费用
    private static final int COST = 0;
    // 消耗的手牌数
    private static final int EXHAUST_AMOUNT = 2;
    // 获得的能量
    private static final int ENERGY_GAIN = 2;

    public BurnTheOldSelf() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        // 未升级时自身消耗
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 先选择并消耗 2 张手牌，再获得能量
        AbstractDungeon.actionManager.addToBottom(
                new ExhaustAction(p, p, EXHAUST_AMOUNT, false));
        AbstractDungeon.actionManager.addToBottom(
                new GainEnergyAction(ENERGY_GAIN));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BurnTheOldSelf();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级后不再消耗自身
            this.exhaust = false;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
