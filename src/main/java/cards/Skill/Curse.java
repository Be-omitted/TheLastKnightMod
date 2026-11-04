package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

public class Curse extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Curse";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Curse.png";
    private static final int COST = 1;
    // 易伤与虚弱的层数
    private static final int BASE_DEBUFF = 3;

    public Curse() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseMagicNumber = BASE_DEBUFF;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p,
                        new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p,
                        new WeakPower(m, this.magicNumber, false), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Curse();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级后不再消耗
            this.exhaust = false;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
