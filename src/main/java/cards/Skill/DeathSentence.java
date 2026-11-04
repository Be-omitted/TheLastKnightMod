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
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

public class DeathSentence extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "DeathSentence";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/DeathSentence.png";
    private static final int COST = 1;
    // 给予所有敌人的易伤层数
    private static final int BASE_VULNERABLE = 2;
    private static final int UPGRADE_VULNERABLE = 1;

    public DeathSentence() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = BASE_VULNERABLE;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.getMonsters() == null) {
            return;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null || monster.isDeadOrEscaped()) {
                continue;
            }
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(monster, p,
                            new VulnerablePower(monster, this.magicNumber, false), this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DeathSentence();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_VULNERABLE);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
