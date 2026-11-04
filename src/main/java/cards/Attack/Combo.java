package cards.Attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.CardRarityFilter;
import helper.CardTypeFilter;
import helper.RandomCardHelper;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

public class Combo extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Combo";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Combo.png";
    private static final int COST = 1;
    private static final int BASE_DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 4;
    // 抽牌数
    private static final int BASE_DRAW = 2;
    private static final int UPGRADE_DRAW = 1;

    public Combo() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = BASE_DRAW;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        // 在抽牌堆加入 1 张随机攻击牌
        RandomCardHelper.addRandomCardToPile(RandomCardHelper.PilePosition.DRAW_PILE,
                CardTypeFilter.of(AbstractCard.CardType.ATTACK), CardRarityFilter.any());

        // 抽牌
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction(p, this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Combo();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_DRAW);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
