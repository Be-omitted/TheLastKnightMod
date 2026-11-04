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
import patches.AbstractCardEnum;
import power.FanaticSDelusionPower;
import power.WillPower;

/**
 * 狂徒妄念
 * 意志达到门槛时可打出，获得免死效果。
 */
public class FanaticSDelusion extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "FanaticSDelusion";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/FanaticSDelusion.png";
    // 费用
    private static final int COST = 2;
    // 打出所需的意志层数
    private static final int BASE_WILL_REQUIREMENT = 30;
    // 升级降低的意志门槛
    private static final int UPGRADE_WILL_REQUIREMENT = -5;

    public FanaticSDelusion() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = BASE_WILL_REQUIREMENT;
        this.magicNumber = this.baseMagicNumber;
        // 保留
        this.selfRetain = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) {
            return false;
        }
        // 执行时读取当前意志层数，达到门槛才允许打出
        return p.hasPower(WillPower.BASE_ID)
                && p.getPower(WillPower.BASE_ID).amount >= this.magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new FanaticSDelusionPower(p)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FanaticSDelusion();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级降低意志门槛
            this.upgradeMagicNumber(UPGRADE_WILL_REQUIREMENT);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
