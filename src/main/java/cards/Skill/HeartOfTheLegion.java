package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

/**
 * 初生之赐，大群之心
 * 抽牌、获得能量与随机模肢，随后消耗。
 */
public class HeartOfTheLegion extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "HeartOfTheLegion";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/HeartOfTheLegion.png";
    // 费用
    private static final int COST = 0;
    // 抽牌数与随机模肢数共用同一个数值
    private static final int BASE_AMOUNT = 1;
    // 升级增加的数值
    private static final int UPGRADE_AMOUNT = 1;
    // 获得的能量
    private static final int ENERGY_GAIN = 1;

    public HeartOfTheLegion() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = BASE_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 抽牌
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction(p, this.magicNumber));
        // 获得能量
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_GAIN));
        // 获得随机模肢
        for (int i = 0; i < this.magicNumber; i++) {
            TheLastKnightModHelper.ModULimbBasePowerPool(p,
                    TheLastKnightModHelper.ModULimbBasePowerPoolIsAll(p));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HeartOfTheLegion();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级提高抽牌数，同时提高随机模肢数
            this.upgradeMagicNumber(UPGRADE_AMOUNT);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
