package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import pathes.AbstractCardEnum;
import power.RedundantVestigePower;

/**
 * 永燃不休
 * 获得能量与模肢:冗痕，使用后回到抽牌堆。
 */
public class EternalBlaze extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "EternalBlaze";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/EternalBlaze.png";
    // 费用
    private static final int COST = 0;
    // 获得的能量
    private static final int ENERGY_GAIN = 2;
    // 获得的模肢:冗痕层数
    private static final int BASE_VESTIGE = 4;
    // 升级降低的冗痕层数
    private static final int UPGRADE_VESTIGE = -1;

    public EternalBlaze() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = BASE_VESTIGE;
        this.magicNumber = this.baseMagicNumber;
        // 打出结算后由 UseCardAction 将这张牌放回抽牌堆
        this.shuffleBackIntoDrawPile = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_GAIN));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p,
                        new RedundantVestigePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new EternalBlaze();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级降低冗痕层数
            this.upgradeMagicNumber(UPGRADE_VESTIGE);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
