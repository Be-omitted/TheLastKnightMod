package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;
import power.LysosomalInjectionGlandPower;

/**
 * 腐蚀液
 * 获得模肢:溶注腺，攻击时附加中毒。
 */
public class CorrosiveLiquid extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "CorrosiveLiquid";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    // 费用
    private static final int COST = 1;
    // 可触发的次数
    private static final int TRIGGER_TIMES = 3;
    // 升级增加的中毒层数
    private static final int Poison_up = 2;

    // 中毒层数
    private int Poison;

    public CorrosiveLiquid() {
        super(ID, cardStrings.NAME, "img/card/CorrosiveLiquid.png", COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = TRIGGER_TIMES;
        this.magicNumber = this.baseMagicNumber;
        this.Poison = 4;
        this.updateRawDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new LysosomalInjectionGlandPower(p, this.magicNumber, this.Poison), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CorrosiveLiquid();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.Poison += Poison_up;
            this.updateRawDescription();
        }
    }

    private void updateRawDescription() {
        String baseDescription = this.upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
        this.rawDescription = baseDescription
                + this.Poison
                + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }
}
