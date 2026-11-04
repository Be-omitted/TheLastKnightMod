package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
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
 * 昭然若揭
 * 失去意志，获得随机模肢。
 */
public class Manifest extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Manifest";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Manifest.png";
    // 费用
    private static final int COST = 1;
    // 失去的意志层数
    private static final int WILL = 2;
    // 升级后失去的意志层数
    private static final int UP_WILL = 1;
    // 获得的随机模肢层数
    private static final int BASE_GAIN = 3;

    public Manifest() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_GAIN;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, WillPower.BASE_ID, (this.upgraded)?UP_WILL:WILL));
        for (int i = 0; i < this.magicNumber; i++) {
            TheLastKnightModHelper.ModULimbBasePowerPool(p, TheLastKnightModHelper.ModULimbBasePowerPoolIsAll(p));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Manifest();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
