package cards.Status;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import helper.TheLastKnightModHelper;

public class Exhaustion extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Exhaustion";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int TEMP_STRENGTH_LOSS = 1;

    public Exhaustion() {
        super(ID, cardStrings.NAME, "img/card/Exhaustion.png", 1, cardStrings.DESCRIPTION,
                CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.exhaust = true;
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerWhenDrawn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) {
            return;
        }
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        p,
                        p,
                        new StrengthPower(p, -TEMP_STRENGTH_LOSS),
                        -TEMP_STRENGTH_LOSS));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        p,
                        p,
                        new LoseStrengthPower(p, -TEMP_STRENGTH_LOSS),
                        -TEMP_STRENGTH_LOSS));
    }

    @Override
    public AbstractCard makeCopy() {
        // 复制卡牌时创建同类型的新实例
        return new Exhaustion();
    }

    @Override
    public void upgrade() {
    }
}
