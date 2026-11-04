package cards.Skill;

import cards.Virtue;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;
import power.WillPower;

/**
 * 谦卑
 * 不能被打出，被丢弃或消耗时抽牌并获得意志。
 */
public class Humility extends Virtue {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Humility";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Humility.png";
    // 无法打出的费用标记
    private static final int COST = -2;
    // 从手牌被丢弃时抽取的牌数
    private static final int DRAW_AMOUNT = 3;
    // 获得的意志层数
    private static final int BASE_WILL = 2;

    public Humility() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.NONE);
        // 尝试打出时显示的原因
        this.cantUseMessage = "不能被打出";

        this.baseMagicNumber = BASE_WILL;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        // 仅设置 -2 费用不足以阻止打出，需要显式返回 false
        return false;
    }

    @Override
    public void triggerOnManualDiscard() {
        if (AbstractDungeon.player == null) {
            return;
        }
        // 升级后先把本牌移出弃牌堆，再抽牌，避免抽牌触发洗牌时把自己抽回
        if (this.upgraded) {
            moveToDrawPileBottom(AbstractDungeon.player.discardPile);
        }
        triggerCommonEffect();
    }

    @Override
    public void triggerOnExhaust() {
        if (AbstractDungeon.player == null) {
            return;
        }

        // 消耗与丢弃共用的抽牌、获得意志
        triggerCommonEffect();

        if (!this.upgraded) {
            return;
        }
        // triggerOnExhaust 触发时本牌尚未进入消耗堆，因此把 MoveCardsAction 排到后续行动中
        moveToDrawPileBottom(AbstractDungeon.player.exhaustPile);
    }

    // 把本牌从指定牌堆移到抽牌堆底
    // MoveCardsAction 默认放入抽牌堆顶部，再在回调中移到堆底
    private void moveToDrawPileBottom(CardGroup source) {
        AbstractDungeon.actionManager.addToBottom(new MoveCardsAction(
                AbstractDungeon.player.drawPile,
                source,
                card -> card == this,
                1,
                cards -> cards.forEach(card -> AbstractDungeon.player.drawPile.moveToBottomOfDeck(card))));
    }

    // 从手牌被丢弃或消耗时共用的结算
    private void triggerCommonEffect() {
        if (AbstractDungeon.player == null) {
            return;
        }
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction(AbstractDungeon.player, DRAW_AMOUNT));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new WillPower(AbstractDungeon.player, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Humility();
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
