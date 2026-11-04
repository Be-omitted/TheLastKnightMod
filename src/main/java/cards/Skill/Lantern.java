package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

public class Lantern extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Lantern";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Lantern.png";
    private static final int COST = 1;
    // 获得的格挡
    private static final int BASE_BLOCK = 9;
    private static final int UPGRADE_BLOCK = 6;
    // 给予所有敌人的虚弱层数
    private static final int BASE_WEAK = 1;

    public Lantern() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = BASE_BLOCK;
        this.baseMagicNumber = BASE_WEAK;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

        if (AbstractDungeon.getMonsters() == null) {
            return;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null || monster.isDeadOrEscaped()) {
                continue;
            }
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(monster, p,
                            new WeakPower(monster, this.magicNumber, false), this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Lantern();
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
