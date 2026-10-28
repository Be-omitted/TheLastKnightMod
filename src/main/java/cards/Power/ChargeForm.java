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
import power.ChargeFormPower;

public class ChargeForm extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "ChargeForm";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/ChargeForm.png";
    private static final int COST = 3;
    // 单次触发对应的弃牌/抽牌数
    private static final int BASE_AMOUNT = 2;
    private static final int UPGRADE_AMOUNT = 1;

    public ChargeForm() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = BASE_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p,
                        new ChargeFormPower(p, this.magicNumber),
                        this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ChargeForm();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级提高层数，即每张牌的弃牌/抽牌数
            this.upgradeMagicNumber(UPGRADE_AMOUNT);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
