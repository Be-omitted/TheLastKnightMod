package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;
import power.RedundantVestigePower;

/**
 * 刮骨
 * 付出生命，清除模肢:冗痕。
 */
public class BoneCurettage extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "BoneCurettage";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/BoneCurettage.png";
    // 费用
    private static final int COST = 0;
    // 失去的生命值
    private static final int HP_LOSS = 5;
    // 清除的模肢:冗痕层数
    private static final int BASE_REMOVE = 10;
    // 升级增加的清除层数
    private static final int UPGRADE_REMOVE = 5;

    public BoneCurettage() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_REMOVE;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 先付出生命代价，生命损失无视格挡
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, HP_LOSS));

        // 再清除至多 magicNumber 层模肢:冗痕，没有冗痕时不产生额外效果
        if (!p.hasPower(RedundantVestigePower.BASE_ID)) {
            return;
        }
        AbstractPower vestige = p.getPower(RedundantVestigePower.BASE_ID);
        int removeAmount = Math.min(this.magicNumber, vestige.amount);
        if (removeAmount > 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(p, p, RedundantVestigePower.BASE_ID, removeAmount));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BoneCurettage();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级提高清除的冗痕层数
            this.upgradeMagicNumber(UPGRADE_REMOVE);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
