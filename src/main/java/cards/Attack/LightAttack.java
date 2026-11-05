package cards.Attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

/**
 * 轻击
 * 费用随模肢层数提高的零费攻击牌。
 */
public class LightAttack extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "LightAttack";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/LightAttack.png";
    // 费用
    private static final int COST = 0;
    // 基础伤害
    private static final int BASE_DAMAGE = 9;
    // 升级增加的伤害
    private static final int UPGRADE_DAMAGE = 3;

    public LightAttack() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.TheLastKnight_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        // 统计玩家身上的“模肢”层数，没有玩家对象时按 0 处理
        int mul = 0;
        if (AbstractDungeon.player != null) {
            mul = TheLastKnightModHelper.GetModULimbBasePowerNumber(AbstractDungeon.player);
        }
        this.setCostForTurn(this.cost + (mul/4));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对选中的敌人造成伤害，数值取经过加成的 damage
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightAttack();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
