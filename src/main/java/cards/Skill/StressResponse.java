package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

/**
 * 应激反应
 * 按模肢层数获得格挡与临时荆棘。
 */
public class StressResponse extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "StressResponse";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/StressResponse.png";
    // 费用
    private static final int COST = 1;
    // 每层模肢获得的基础格挡
    private static final int BASE_BLOCK = 2;
    // 升级增加的格挡
    private static final int UPGRADE_BLOCK = 1;
    // 每层模肢获得的临时荆棘
    private static final int BASE_THORNS = 1;

    public StressResponse() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = BASE_BLOCK;
        this.baseMagicNumber = BASE_THORNS;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 按当前模肢层数结算，没有模肢时不产生效果
        int limbCount = TheLastKnightModHelper.GetModULimbBasePowerNumber(p);
        if (limbCount <= 0) {
            return;
        }

        int totalBlock = this.block * limbCount;
        int totalThorns = this.magicNumber * limbCount;

        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, totalBlock));
        // 获得荆棘，并在下个回合开始时移除本次获得的层数，使其成为“临时荆棘”
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p,
                        new ThornsPower(p, totalThorns), totalThorns));
        AbstractDungeon.actionManager.addToTurnStart(
                new ReducePowerAction(p, p, ThornsPower.POWER_ID, totalThorns));
    }

    @Override
    public AbstractCard makeCopy() {
        return new StressResponse();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
