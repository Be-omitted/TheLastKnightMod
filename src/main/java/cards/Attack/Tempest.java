package cards.Attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

public class Tempest extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Tempest";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Tempest.png";
    // X 费
    private static final int COST = -1;
    // 每次结算对所有敌人造成的伤害
    private static final int BASE_DAMAGE = 3;
    private static final int UPGRADE_DAMAGE = 1;
    // 每次结算获得的格挡
    private static final int BASE_BLOCK = 3;
    private static final int UPGRADE_BLOCK = 1;
    private static final String CHEMICAL_X = "Chemical X";

    public Tempest() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.baseBlock = BASE_BLOCK;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = this.energyOnUse;
        if (x == -1) {
            x = EnergyPanel.totalCount;
        }
        if (p.hasRelic(CHEMICAL_X)) {
            x += 2;
            p.getRelic(CHEMICAL_X).flash();
        }

        // 对所有敌人造成 X 次伤害
        for (int i = 0; i < x; i++) {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }

        // 获得 X 次格挡
        for (int i = 0; i < x; i++) {
            AbstractDungeon.actionManager.addToBottom(
                    new GainBlockAction(p, p, this.block));
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Tempest();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeBlock(UPGRADE_BLOCK);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
